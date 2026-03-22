package uk.ac.bangor.cs.group2.academicymraeg.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		List<QuestionBlueprint> questionPool = buildQuestionPool(nouns);
		
		if (questionPool.size() < 20) {
			throw new IllegalStateException("Not enough questions, please contact your instructor or administrator");
		}
		
		Collections.shuffle(questionPool);
		
		Test test = new Test(0, username, 0, LocalDateTime.now());
		Test savedTest = testRepository.save(test);
		
		for (int i = 0; i < 20; i++) {
            QuestionBlueprint blueprint = questionPool.get(i);

            List<String> options = buildOptions(blueprint, nouns);
            Collections.shuffle(options);

            String optionA = options.get(0);
            String optionB = options.get(1);
            String optionC = options.size() > 2 ? options.get(2) : null;

            TestQuestions testQuestion = new TestQuestions(
                    0,
                    savedTest,
                    blueprint.questionText(),
                    i + 1,
                    optionA,
                    optionB,
                    optionC,
                    blueprint.correctAnswer(),
                    null,
                    false
            );
            
            testQuestionRepository.save(testQuestion);
		}
		return savedTest;
	}
		
	
    public Test getTestById(long testId) {
        return testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found"));
    }

    public List<TestQuestions> getQuestionsForTest(Test test) {
        return testQuestionRepository.findByTestOrderByPositionAsc(test);
    }

    @Transactional
    public void submitTest(long testId, List<String> answers) {
        Test test = getTestById(testId);
        List<TestQuestions> questions = getQuestionsForTest(test);

        if (answers.size() != questions.size()) {
            throw new IllegalArgumentException("Number of answers does not match number of questions");
        }

        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            TestQuestions question = questions.get(i);
            String userAnswer = answers.get(i);

            question.setUserAnswer(userAnswer);

            boolean isCorrect = userAnswer != null
                    && userAnswer.equalsIgnoreCase(question.getCorrectAnswer());

            question.setCorrect(isCorrect);

            if (isCorrect) {
                score++;
            }

            testQuestionRepository.save(question);
        }

        test.setResult(score);
        testRepository.save(test);
    }
	
	private List<QuestionBlueprint> buildQuestionPool(List<Noun> nouns) {
        List<QuestionBlueprint> pool = new ArrayList<>();

        for (Noun noun : nouns) {
            if (noun.getEnglish() != null && noun.getWelsh() != null && noun.getGender() != null) {
                pool.add(new QuestionBlueprint(
                        Question.QuestionType.ENGLISH,
                        "What is the meaning (English) of \"" + noun.getWelsh() + "\"?",
                        noun.getEnglish()
                ));

                pool.add(new QuestionBlueprint(
                        Question.QuestionType.WELSH,
                        "What is the Welsh for \"" + noun.getEnglish() + "\"?",
                        noun.getWelsh()
                ));

                pool.add(new QuestionBlueprint(
                        Question.QuestionType.GENDER,
                        "What gender is the Welsh word \"" + noun.getWelsh() + "\"?",
                        noun.getGender().name()
                ));
            }
        }

        return pool;
    }
	
	
    private List<String> buildOptions(QuestionBlueprint blueprint, List<Noun> nouns) {
        List<String> options = new ArrayList<>();
        options.add(blueprint.correctAnswer());

        switch (blueprint.questionType()) {
            case ENGLISH:
                options.addAll(getWrongEnglishAnswers(blueprint.correctAnswer(), nouns));
                break;
            case WELSH:
                options.addAll(getWrongWelshAnswers(blueprint.correctAnswer(), nouns));
                break;
            case GENDER:
                options.addAll(getWrongGenderAnswers(blueprint.correctAnswer()));
                break;
            default:
                throw new IllegalArgumentException("Unsupported question type");
        }

        return options;
    }

    private List<String> getWrongEnglishAnswers(String correctAnswer, List<Noun> nouns) {
        Set<String> wrongAnswers = new HashSet<>();

        for (Noun noun : nouns) {
            if (noun.getEnglish() != null && !noun.getEnglish().equalsIgnoreCase(correctAnswer)) {
                wrongAnswers.add(noun.getEnglish());
            }
        }

        if (wrongAnswers.size() < 2) {
            throw new IllegalStateException("Not enough distinct English answers for wrong options.");
        }

        List<String> wrongList = new ArrayList<>(wrongAnswers);
        Collections.shuffle(wrongList);

        return wrongList.subList(0, 2);
    }

    private List<String> getWrongWelshAnswers(String correctAnswer, List<Noun> nouns) {
        Set<String> wrongAnswers = new HashSet<>();

        for (Noun noun : nouns) {
            if (noun.getWelsh() != null && !noun.getWelsh().equalsIgnoreCase(correctAnswer)) {
                wrongAnswers.add(noun.getWelsh());
            }
        }

        if (wrongAnswers.size() < 2) {
            throw new IllegalStateException("Not enough distinct Welsh answers for wrong options.");
        }

        List<String> wrongList = new ArrayList<>(wrongAnswers);
        Collections.shuffle(wrongList);

        return wrongList.subList(0, 2);
    }

    private List<String> getWrongGenderAnswers(String correctAnswer) {
        List<String> wrongAnswers = new ArrayList<>();

        if ("MASCULINE".equalsIgnoreCase(correctAnswer)) {
            wrongAnswers.add("FEMININE");
        } else if ("FEMININE".equalsIgnoreCase(correctAnswer)) {
            wrongAnswers.add("MASCULINE");
        } else {
            throw new IllegalStateException("Unknown gender: " + correctAnswer);
        }

        return wrongAnswers;
    }

    private record QuestionBlueprint(
            Question.QuestionType questionType,
            String questionText,
            String correctAnswer
    ) {
    }
}