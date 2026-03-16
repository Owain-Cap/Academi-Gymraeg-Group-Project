package uk.ac.bangor.cs.group2.academicymraeg.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class TestQuestions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long testQuestionId;
	
	@ManyToOne
	@JoinColumn(name = "testId")
	private Test test; 
	
	private String question;
	private int posiiton;
	private String optionA;
	private String optionB;
	private String optionC;
	private String correctAnswer;
	private String userAnswer;
	private boolean correct;
	
	public TestQuestions(long testQuestionId, Test test, String question, int posiiton, String optionA, String optionB,
			String optionC, String correctAnswer, String userAnswer, boolean correct) {
		super();
		this.testQuestionId = testQuestionId;
		this.test = test;
		this.question = question;
		this.posiiton = posiiton;
		this.optionA = optionA;
		this.optionB = optionB;
		this.optionC = optionC;
		this.correctAnswer = correctAnswer;
		this.userAnswer = userAnswer;
		this.correct = correct;
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

	public int getPosiiton() {
		return posiiton;
	}

	public void setPosiiton(int posiiton) {
		this.posiiton = posiiton;
	}

	public String getOptionA() {
		return optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public String getOptionB() {
		return optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}

	public String getOptionC() {
		return optionC;
	}

	public void setOptionC(String optionC) {
		this.optionC = optionC;
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

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	@Override
	public String toString() {
		return "TestQuestions [testQuestionId=" + testQuestionId + ", test=" + test + ", question=" + question
				+ ", posiiton=" + posiiton + ", optionA=" + optionA + ", optionB=" + optionB + ", optionC=" + optionC
				+ ", correctAnswer=" + correctAnswer + ", userAnswer=" + userAnswer + ", correct=" + correct + "]";
	}
	
	
	
}
