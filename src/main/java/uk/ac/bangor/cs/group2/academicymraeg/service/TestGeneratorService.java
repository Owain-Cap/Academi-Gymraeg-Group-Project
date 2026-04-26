package uk.ac.bangor.cs.group2.academicymraeg.service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bangor.cs.group2.academicymraeg.models.Noun;
import uk.ac.bangor.cs.group2.academicymraeg.models.Question;
import uk.ac.bangor.cs.group2.academicymraeg.models.Test;
import uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions;
import uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions.AnswerStatus;
import uk.ac.bangor.cs.group2.academicymraeg.repository.NounRepository;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestQuestionRepository;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestRepository;


/**
 * Service responsible for generating and submitting tests.
 * 
 * <p>This service handles the full lifecycle of a test including:
 * <ul>
 *     <li>Generating new tests with randomised questions</li>
 *     <li>Resuming existing in-progress tests</li>
 *     <li>Automatically submitting expired tests</li>
 *     <li>Validating and marking submitted answers</li>
 * </ul>
 * 
 * <p>Tests are generated using a random selection of nouns from the database,
 * with a fixed distribution of question types (English, Welsh, and Gender).
 * 
 * <p>Answer evaluation includes support for Welsh circumflexes, allowing answers
 * to be marked correct even if accents are missing, while optionally flagging
 * this to the user.
 */
@Service
public class TestGeneratorService {
	
    /**
     * Constructs the TestGeneratorService with required repositories.
     *
     * @param nounRepository repository for accessing noun data
     * @param testRepository repository for storing and retrieving tests
     * @param testQuestionRepository repository for storing and retrieving test questions
     */
	
	private NounRepository nounRepository;
	private TestRepository testRepository;
	private TestQuestionRepository testQuestionRepository;
	private static final String SKIPPED = "SKIPPED_BY_USER";

	private enum AnswerCheckResult {
		CORRECT_EXACT, CORRECT_WITHOUT_DIACRITICS, INCORRECT, SKIPPED
	}

	public TestGeneratorService(NounRepository nounRepository, TestRepository testRepository,
			TestQuestionRepository testQuestionRepository) {
		this.nounRepository = nounRepository;
		this.testRepository = testRepository;
		this.testQuestionRepository = testQuestionRepository;
	}

    /**
     * Generates a new test for a user, or resumes an existing one.
     *
     * If the user already has an in-progress test:
     * 		If the test is still valid (not expired), it is returned
     * 		If the test has expired, it is automatically submitted and a new test is generated
     * 
     *
     * A new test consists of 20 randomly selected nouns, with: 7 English-to-Welsh questions
     * 7 Welsh-to-English questions, 6 gender identification questions
     *
     * @param username the user requesting the test
     * @return a new or existing in-progress Test
     * @throws IllegalStateException if there are insufficient valid nouns available
     */
	@Transactional
	public Test generateTestForUser(String username) {
		//check for tests in progress
		var existingTest = testRepository.findByUsernameAndStatus(username, Test.TestStatus.IN_PROGRESS);
		// resume existing test if one exists rather than generating new.
		if (existingTest.isPresent()) {
			Test test = existingTest.get();
			
			if (LocalDateTime.now().isBefore(test.getExpiresAt())) {
	            return test; // if test is still valid return it
	        } else {
	        	//if test is expired autosubmit it
	        	getTestById(test.getTestId());
	        }
	    }
		
		List<Noun> nouns = nounRepository.findAll();
		List<Noun> validNouns = new ArrayList<>();

		
		//add all valid nouns from DB to list called validNouns
		for (Noun noun : nouns) {
			if (noun.getEnglish() != null && noun.getWelsh() != null && noun.getGender() != null) {
				validNouns.add(noun);
			}
		}
		//check there are 20 nouns available
		if (validNouns.size() < 20) {
			throw new IllegalStateException("Not enough nouns available for test, please speak to your instructor");
		}
		
		//shuffle the list to randomise the nouns
		Collections.shuffle(validNouns);
		
		List<Noun> selectedNouns = validNouns.subList(0,  20);
		
		//create a list of questions;
		// 7 welsh to english, 7 english to welsh and 6 gender questions
		List<Question.QuestionType> questionTypes = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			questionTypes.add(Question.QuestionType.ENGLISH);
		}
		for (int i = 0; i < 7; i++) {
			questionTypes.add(Question.QuestionType.WELSH);
		}
		for (int i = 0; i < 6; i++) {
			questionTypes.add(Question.QuestionType.GENDER);
		}
		
		//shuffle questions to randomise the order
		Collections.shuffle(questionTypes);
		
		//create the questions and answer combinations in a list called questionList
		List<QuestionBlueprint> questionList = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			questionList.add(buildQuestionList(selectedNouns.get(i), questionTypes.get(i)));
		}

		//shuffle questions again to further randomise
		Collections.shuffle(questionList);


		// save test in db 
		//set 30 min timer and in progress status
		LocalDateTime testStarted = LocalDateTime.now();

		Test test = new Test(
			0,
			username,
			0,
			testStarted,
			Test.TestStatus.IN_PROGRESS,
			testStarted.plusMinutes(30)
		);

		Test savedTest = testRepository.save(test);

		
		//create multiple choice for gender type questions
		for (int i = 0; i < 20; i++) {
			QuestionBlueprint blueprint = questionList.get(i);

			String optionA = null;
			String optionB = null;

			if (blueprint.questionType() == Question.QuestionType.GENDER) {
				optionA = "MASCULINE";
				optionB = "FEMININE";
			}

			//save questions in db
			TestQuestions testQuestion = new TestQuestions(0, savedTest, blueprint.questionText(),
					blueprint.questionType(), i + 1, optionA, optionB, blueprint.correctAnswer(), null,
					TestQuestions.AnswerStatus.NOT_ANSWERED, false);

			testQuestionRepository.save(testQuestion);
		}
		return savedTest;
	}
	
    /**
     * Retrieves a test by its ID.
     *
     * If the test is still marked as in-progress but has exceeded its expiry time,
     * it is automatically submitted. Any unanswered questions are marked as skipped.
     *
     * @param testId the ID of the test
     * @return the Test object
     * @throws IllegalArgumentException if the test does not exist
     */
	public Test getTestById(long testId) {
		Test test = testRepository.findById(testId).orElseThrow(() -> new IllegalArgumentException("Test not found"));
		
		//if the test was started more than 30 minutes ago, automatically submit it to the database
		if (test.getStatus() == Test.TestStatus.IN_PROGRESS && LocalDateTime.now().isAfter(test.getExpiresAt())) {
			
			List<TestQuestions> questions = getQuestionsForTest(test);
			
			for (TestQuestions q : questions) {
				if (q.getUserAnswer() == null) {
					//mark unanswered questions as skipped
					q.setUserAnswer(SKIPPED); 
					q.setAnswerStatus(AnswerStatus.SKIPPED);
					
					testQuestionRepository.save(q);
				}
			}
			//save test in database
			test.setStatus(Test.TestStatus.SUBMITTED);
			testRepository.save(test);
		}
		return test;
	}
	
    /**
    * Retrieves all questions associated with a given test, ordered by position.
    *
    * @param test the test whose questions are to be retrieved
    * @return list of TestQuestions in order
    */
	public List<TestQuestions> getQuestionsForTest(Test test) {
		return testQuestionRepository.findByTestOrderByPositionAsc(test);
	}

	
    /**
     * Submits a completed test and marks the answers.
     *
     * This method:
     * 		Validates all submitted answers
     *     	Marks each question as correct, incorrect, or skipped
     *     	Applies diacritic-aware comparison for translation questions
     *     	Calculates and stores the final score
     *     	Updates the test status to SUBMITTED
     *
     * @param testId the ID of the test being submitted
     * @param answers list of user-submitted answers
     * @throws IllegalStateException if the test has already been submitted
     * @throws IllegalArgumentException if validation fails
     */
	@Transactional
	public void submitTest(long testId, List<String> answers) {
		Test test = getTestById(testId);
		
		//stop a user editting the test after submission
		if (test.getStatus() == Test.TestStatus.SUBMITTED) {
			throw new IllegalStateException("This test has already been subbmitted");
		}
		
		List<TestQuestions> questions = getQuestionsForTest(test);

		validateSubmittedAnswers(questions, answers);

		int score = 0;

		for (int i = 0; i < questions.size(); i++) {
			TestQuestions question = questions.get(i);

			String userAnswer = answers.get(i);

			if (userAnswer != null) {
				userAnswer = userAnswer.trim();
			}

			question.setUserAnswer(userAnswer);

			AnswerCheckResult result = checkAnswer(userAnswer, question.getCorrectAnswer(), question.getQuestionType());

			switch (result) {
			case SKIPPED:
				question.setAnswerStatus(AnswerStatus.SKIPPED);
				question.setDiacriticReminder(false);
				break;

			case CORRECT_EXACT:
				question.setAnswerStatus(AnswerStatus.CORRECT);
				question.setDiacriticReminder(false);
				score++;
				break;

			case CORRECT_WITHOUT_DIACRITICS:
				question.setAnswerStatus(AnswerStatus.CORRECT);
				question.setDiacriticReminder(true);
				score++;
				break;

			case INCORRECT:
			default:
				question.setAnswerStatus(AnswerStatus.INCORRECT);
				question.setDiacriticReminder(false);
				break;
			}

			testQuestionRepository.save(question);
		}

		test.setResult(score);
		test.setStatus(Test.TestStatus.SUBMITTED);
		testRepository.save(test);

	}

    /**
     * Validates the list of submitted answers against the test questions.
     *
     *All questions are answered or explicitly skipped
     *Answers are not empty
     *Answers meet format and length requirements
     *Gender answers are restricted to valid values
     *
     * @param questions the list of test questions
     * @param answers the list of submitted answers
     * @throws IllegalArgumentException if any validation rule is violated
     */
	private void validateSubmittedAnswers(List<TestQuestions> questions, List<String> answers) {
		// make sure answers aren't empty or are intentional skipped
		if (answers == null || answers.size() < questions.size()) {
			throw new IllegalArgumentException("Please answer every question, or click skip, before submitting.");
		}

		for (int i = 0; i < questions.size(); i++) {
			TestQuestions question = questions.get(i);
			String answer = answers.get(i);

			// make sure answer isn't empty
			if (answer == null || answer.trim().isEmpty()) {
				throw new IllegalArgumentException(
						"Question " + (i + 1) + " must be answered or skipped by clicking 'skip'.");
			}

			String cleanAns = answer.trim().toUpperCase();

			// if answer was skipped, end validation.
			if (cleanAns.equals(SKIPPED)) {
				continue;
			}

			if (question.getQuestionType() == Question.QuestionType.GENDER) {
				// Validate gender questions

				if (!cleanAns.equals("MASCULINE") && !cleanAns.equals("FEMININE")) {
					throw new IllegalArgumentException("Invalid answer for question " + (i + 1) + ".");
				}
			} else {
				// Validate translation questions
				String trimmed = answer.trim();

				// length check
				if (trimmed.length() > 60) {
					throw new IllegalArgumentException("Answer for question " + (i + 1) + " is too long");
				}

				// character validation (letters + diacritics + spaces only)
				if (!trimmed.matches("^[\\p{L}\\s]+$")) {
					throw new IllegalArgumentException(
						"Answer for question " + (i + 1) + " contains invalid characters. Only letters are allowed.");
				}
			}
		}
	}
	
    /**
     * Compares a user’s answer to the correct answer.
     *
     * Checks for:
     * 		Exact match (including diacritics)
     * 		Match ignoring diacritics
     * 		Incorrect answer
     * 		Skipped answer
     *
     * @param userAnswer the answer provided by the user
     * @param correctAnswer the correct answer
     * @param questionType the type of question being evaluated
     * @return the result of the comparison
     */
	private AnswerCheckResult checkAnswer(String userAnswer, String correctAnswer, Question.QuestionType questionType) {
		if (userAnswer == null || correctAnswer == null || userAnswer.isBlank()) {
			return AnswerCheckResult.INCORRECT;
		}
		
		String cleanUserAns = userAnswer.trim();
		String cleanCorrectAns = correctAnswer.trim();

		if (cleanUserAns.equalsIgnoreCase(SKIPPED)) {
			return AnswerCheckResult.SKIPPED;
		}
		
		String exactUserAns = Normalizer.normalize(cleanUserAns, Normalizer.Form.NFC);
		String exactCorrectAns = Normalizer.normalize(cleanCorrectAns, Normalizer.Form.NFC);

		if (exactUserAns.equalsIgnoreCase(exactCorrectAns)) {
		    return AnswerCheckResult.CORRECT_EXACT;
		}
		
		if (questionType == Question.QuestionType.GENDER) {
			return AnswerCheckResult.INCORRECT;
		}
		
		//remove diacritics
		String normalisedUserAns = Normalizer.normalize(cleanUserAns, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		String normalisedCorrectAns = Normalizer.normalize(cleanCorrectAns, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		
		if (normalisedUserAns.equalsIgnoreCase(normalisedCorrectAns)) {
			return AnswerCheckResult.CORRECT_WITHOUT_DIACRITICS;
		}
		
		return AnswerCheckResult.INCORRECT;
		

	}

    /**
     * Constructs a question blueprint based on a noun and question type.
     *
     * @param noun the noun used in the question
     * @param questionType the type of question to generate
     * @return a QuestionBlueprint containing the question data
     */
	private QuestionBlueprint buildQuestionList(Noun noun,  Question.QuestionType questionType) {
		switch (questionType) {
		case ENGLISH:
			return new QuestionBlueprint(Question.QuestionType.ENGLISH,
					"What is the meaning of \"" + noun.getWelsh() + "\"?", noun.getEnglish());

		case WELSH:
			return new QuestionBlueprint(Question.QuestionType.WELSH,
					"What is the Welsh for \"" + noun.getEnglish() + "\"?", noun.getWelsh());

		case GENDER:
			return new QuestionBlueprint(Question.QuestionType.GENDER,
					"What gender is the word \"" + noun.getWelsh() + "\"?", noun.getGender().name());

		default:
			throw new IllegalArgumentException("Unsupported question type");
		}
	}

	
	private record QuestionBlueprint(Question.QuestionType questionType, String questionText, String correctAnswer) {
	}
	
    /**
     * Checks whether a user currently has an active (in-progress) test.
     *
     * @param username the user to check
     * @return true if an in-progress test exists, false otherwise
     */
	public boolean hasActiveTest(String username) {
		//check if a user has an in progress test active
		return testRepository.existsByUsernameAndStatus(username, Test.TestStatus.IN_PROGRESS);
	}
	
    /**
     * Retrieves the active (in-progress) test for a user, if one exists.
     *
     * @param username the user whose test is being retrieved
     * @return the active Test, or null if none exists
     */
	public Test getActiveTest(String username) {
		//get in progress tests for the user
		return testRepository.findByUsernameAndStatus(username, Test.TestStatus.IN_PROGRESS).orElse(null);
	}
}