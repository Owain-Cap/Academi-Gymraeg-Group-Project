package uk.ac.bangor.cs.group2.academicymraeg;

import java.time.LocalDateTime;

public class Answer {

	private long answerID;
	private Test test;
	private Question question;
	private String studentAnswer;
	private boolean isCorrect;
	private LocalDateTime answeredAt;
	
	public Answer() {
		
	}

	public Answer(long answerID, Test test, Question question, String studentAnswer, boolean isCorrect,
			LocalDateTime answeredAt) {
		super();
		this.answerID = answerID;
		this.test = test;
		this.question = question;
		this.studentAnswer = studentAnswer;
		this.isCorrect = isCorrect;
		this.answeredAt = answeredAt;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getStudentAnswer() {
		return studentAnswer;
	}

	public void setStudentAnswer(String studentAnswer) {
		this.studentAnswer = studentAnswer;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public LocalDateTime getAnsweredAt() {
		return answeredAt;
	}

	public void setAnsweredAt(LocalDateTime answeredAt) {
		this.answeredAt = answeredAt;
	}

	public long getAnswerID() {
		return answerID;
	}


	@Override
	public String toString() {
		return "Answer [answerID=" + answerID + ", test=" + test + ", question=" + question + ", studentAnswer="
				+ studentAnswer + ", isCorrect=" + isCorrect + ", answeredAt=" + answeredAt + "]";
	}
	
}
