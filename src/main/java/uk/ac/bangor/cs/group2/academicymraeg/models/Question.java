package uk.ac.bangor.cs.group2.academicymraeg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long questionID;
	
	@ManyToOne
	@JoinColumn(name = "nounID")
	private Noun noun;
	public enum QuestionType{GENDER,ENGLISH,WELSH};

	private QuestionType questionType;
	private String questionText;
	private String correctAnswer;

	
	public Question() {
		
	}

	public Question(long questionID, Noun noun, QuestionType questionType, String questionText, String correctAnswer) {
		super();
		this.questionID = questionID;
		this.noun = noun;
		this.questionType = questionType;
		this.questionText = questionText;
		this.correctAnswer = correctAnswer;
	}

	public Noun getNoun() {
		return noun;
	}

	public void setNoun(Noun noun) {
		this.noun = noun;
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

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public long getQuestionID() {
		return questionID;
	}

	@Override
	public String toString() {
		return "Question [questionID=" + questionID + ", noun=" + noun + ", questionType=" + questionType
				+ ", questionText=" + questionText + ", correctAnswer=" + correctAnswer
				+ "]";
	}


	
}
