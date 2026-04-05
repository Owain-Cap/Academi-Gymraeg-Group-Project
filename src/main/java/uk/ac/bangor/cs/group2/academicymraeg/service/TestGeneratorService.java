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

@Service
public class TestGeneratorService {
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

	@Transactional
	public Test generateTestForUser(String username) {
		List<Noun> nouns = nounRepository.findAll();
		List<Noun> validNouns = new ArrayList<>();

		for (Noun noun : nouns) {
			if (noun.getEnglish() != null && noun.getWelsh() != null && noun.getGender() != null) {
				validNouns.add(noun);
			}
		}
		if (validNouns.size() < 20) {
			throw new IllegalStateException("Not enough nouns available for test, please speak to your instructor");
		}
		
		Collections.shuffle(validNouns);
		
		List<Noun> selectedNouns = validNouns.subList(0,  20);
		
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
		
		Collections.shuffle(questionTypes);
		
		List<QuestionBlueprint> questionList = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			questionList.add(buildQuestionList(selectedNouns.get(i), questionTypes.get(i)));
		}

		Collections.shuffle(questionList);


		

		// add test date-time
		Test test = new Test(0, username, 0, LocalDateTime.now());
		Test savedTest = testRepository.save(test);

		for (int i = 0; i < 20; i++) {
			QuestionBlueprint blueprint = questionList.get(i);

			String optionA = null;
			String optionB = null;

			if (blueprint.questionType() == Question.QuestionType.GENDER) {
				optionA = "MASCULINE";
				optionB = "FEMININE";
			}

			TestQuestions testQuestion = new TestQuestions(0, savedTest, blueprint.questionText(),
					blueprint.questionType(), i + 1, optionA, optionB, blueprint.correctAnswer(), null,
					TestQuestions.AnswerStatus.NOT_ANSWERED, false);

			testQuestionRepository.save(testQuestion);
		}
		return savedTest;
	}

	public Test getTestById(long testId) {
		return testRepository.findById(testId).orElseThrow(() -> new IllegalArgumentException("Test not found"));
	}

	public List<TestQuestions> getQuestionsForTest(Test test) {
		return testQuestionRepository.findByTestOrderByPositionAsc(test);
	}

	@Transactional
	public void submitTest(long testId, List<String> answers) {
		Test test = getTestById(testId);
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
		testRepository.save(test);
	}

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
}