package uk.ac.bangor.cs.group2.academicymraeg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long questionId;
	
	public enum QuestionType{GENDER,ENGLISH,WELSH};

	@Enumerated(EnumType.STRING)
	private QuestionType questionType;
	private String questionText;
	
	public Question(long questionId, QuestionType questionType, String questionText) {
		super();
		this.questionId = questionId;
		this.questionType = questionType;
		this.questionText = questionText;
	}

	public long getQuestionId() {
		return questionId;
	}

	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	

	
}
