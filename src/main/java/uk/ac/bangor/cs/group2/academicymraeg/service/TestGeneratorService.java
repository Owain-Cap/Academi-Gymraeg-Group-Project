package uk.ac.bangor.cs.group2.academicymraeg.service;

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
import uk.ac.bangor.cs.group2.academicymraeg.repository.NounRepository;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestQuestionRepository;
import uk.ac.bangor.cs.group2.academicymraeg.repository.TestRepository;

@Service
public class TestGeneratorService {
	private NounRepository nounRepository;
	private TestRepository testRepository;
	private TestQuestionRepository testQuestionRepository;

	public TestGeneratorService(NounRepository nounRepository, TestRepository testRepository,
			TestQuestionRepository testQuestionRepository) {
		this.nounRepository = nounRepository;
		this.testRepository = testRepository;
		this.testQuestionRepository = testQuestionRepository;
	}

	@Transactional
	public Test generateTestForUser(String username) {
		List<Noun> nouns = nounRepository.findAll();

		if (nouns.size() < 3) {
			throw new IllegalStateException("Not enough nouns available for test, please speak to your instructor");
		}
		List<QuestionBlueprint> questionList = buildQuestionList(nouns);

		if (questionList.size() < 20) {
			throw new IllegalStateException("Not enough questions, please contact your instructor or administrator");
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

			TestQuestions testQuestion = new TestQuestions(0, savedTest, blueprint.questionText(), blueprint.questionType(), i + 1, optionA,
					optionB, blueprint.correctAnswer(), null, false);

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

		int score = 0;

		for (int i = 0; i < questions.size(); i++) {
			TestQuestions question = questions.get(i);
			
			String userAnswer = null;

			if (answers != null && i< answers.size()) {
				userAnswer = answers.get(i);
			}
			
			if (userAnswer != null) {
				userAnswer = userAnswer.trim();
			}
			
			question.setUserAnswer(userAnswer);

			boolean isCorrect = answersMatch(userAnswer, question.getCorrectAnswer());
			question.setCorrect(isCorrect);

			if (isCorrect) {
				score++;
			}

			testQuestionRepository.save(question);
		}

		test.setResult(score);
		testRepository.save(test);
	}

	private boolean answersMatch(String userAnswer, String correctAnswer) {
		if (userAnswer == null || correctAnswer == null || userAnswer.isBlank()) {
			return false;
		}
		return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
	}

	private List<QuestionBlueprint> buildQuestionList(List<Noun> nouns) {
		List<QuestionBlueprint> questions = new ArrayList<>();

		for (Noun noun : nouns) {
			if (noun.getEnglish() != null && noun.getWelsh() != null && noun.getGender() != null) {
				questions.add(new QuestionBlueprint(Question.QuestionType.ENGLISH,
						"What is the meaning of \"" + noun.getWelsh() + "\"?", noun.getEnglish()));

				questions.add(new QuestionBlueprint(Question.QuestionType.WELSH,
						"What is the Welsh for \"" + noun.getEnglish() + "\"?", noun.getWelsh()));

				questions.add(new QuestionBlueprint(Question.QuestionType.GENDER,
						"What gender is the word \"" + noun.getWelsh() + "\"?", noun.getGender().name()));
			}
		}

		return questions;
	}

	private record QuestionBlueprint(Question.QuestionType questionType, String questionText, String correctAnswer) {
	}
}