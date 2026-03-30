package uk.ac.bangor.cs.group2.academicymraeg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TestQuestions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long testQuestionId;
	
	@ManyToOne
	@JoinColumn(name = "testId", nullable = false)
	private Test test; 
	
	private String question;
	private int position;
	
	@Enumerated(EnumType.STRING)
	private Question.QuestionType questionType;
	
	
	public enum AnswerStatus {
	    CORRECT,
	    INCORRECT,
	    SKIPPED,
	    NOT_ANSWERED
	}
	
	private String optionA;
	private String optionB;
	private String correctAnswer;
	private String userAnswer;
	
	@Enumerated(EnumType.STRING)
	private AnswerStatus answerStatus;
	
	private boolean diacriticReminder;

	
    protected TestQuestions() {
    }
	
	public TestQuestions(long testQuestionId, Test test, String question, Question.QuestionType questionType, int position, String optionA, String optionB,
			String correctAnswer, String userAnswer, AnswerStatus answerStatus, boolean diacriticReminder) {
		this.testQuestionId = testQuestionId;
		this.test = test;
		this.question = question;
		this.position = position;
		this.questionType = questionType;
		this.optionA = optionA;
		this.optionB = optionB;
		this.correctAnswer = correctAnswer;
		this.userAnswer = userAnswer;
		this.answerStatus = answerStatus;
		this.diacriticReminder = diacriticReminder;
	}

	public boolean isDiacriticReminder() {
		return diacriticReminder;
	}

	public void setDiacriticReminder(boolean diacriticReminder) {
		this.diacriticReminder = diacriticReminder;
	}

	public long getTestQuestionId() {
		return testQuestionId;
	}


	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getOptionA() {
		return optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public Question.QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Question.QuestionType questionType) {
		this.questionType = questionType;
	}

	public String getOptionB() {
		return optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}


	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}


	public AnswerStatus getAnswerStatus() {
		return answerStatus;
	}

	public void setAnswerStatus(AnswerStatus answerStatus) {
		this.answerStatus = answerStatus;
	}

	@Override
	public String toString() {
		return "TestQuestions [testQuestionId=" + testQuestionId + ", test=" + test + ", question=" + question
				+ ", position=" + position + ", questionType=" + questionType + ", optionA=" + optionA + ", optionB="
				+ optionB + ", correctAnswer=" + correctAnswer + ", userAnswer=" + userAnswer + ", answerStatus="
				+ answerStatus + ", diacriticReminder=" + diacriticReminder + "]";
	}



	
	
	
	
}
