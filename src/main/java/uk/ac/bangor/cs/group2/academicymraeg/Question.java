package uk.ac.bangor.cs.group2.academicymraeg;

import java.sql.ResultSet;
import java.util.Collection;

public class Question implements SQLObject<Question> {
	private long questionID;
	private Noun noun;
	public enum QuestionType{
		GENDER,ENGLISH,WELSH
	};

	private QuestionType questionType;
	private String questionText;
	private String correctAnswer;
	private NounBackup nounBackup;
	
	
	public Question() {
		
	}

	public Question(long questionID, Noun noun, QuestionType questionType, String questionText, String correctAnswer,NounBackup nounBackup) {
		super();
		this.questionID = questionID;
		this.noun = noun;
		this.questionType = questionType;
		this.questionText = questionText;
		this.correctAnswer = correctAnswer;
		this.nounBackup = nounBackup;
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

	public NounBackup getNounBackup() {
	    return nounBackup;
	}
	public void setNounBackup(NounBackup nounBackup) {
	    this.nounBackup = nounBackup;
	}

	@Override
	public Collection<Question> fromResultSet(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toDeleteSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toInsertSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toUpdateSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "Question [questionID=" + questionID + ", noun=" + noun + ", questionType=" + questionType
				+ ", questionText=" + questionText + ", correctAnswer=" + correctAnswer + ", nounBackup=" + nounBackup
				+ "]";
	}


	
}
