package uk.ac.bangor.cs.group2.academicymraeg;

import java.sql.ResultSet;
import java.util.Collection;

public class AnswerOption implements SQLObject<AnswerOption> {
	private long optionID;
	
	//foreign key
	private Question question;
	
	private String optionText;
	private boolean isCorrect;
	
	public AnswerOption() {
		
	}

	public AnswerOption(long optionID, Question question, String optionText, boolean isCorrect) {
		super();
		this.optionID = optionID;
		this.question = question;
		this.optionText = optionText;
		this.isCorrect = isCorrect;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public long getOptionID() {
		return optionID;
	}

	@Override
	public Collection<AnswerOption> fromResultSet(ResultSet rs) {
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
		return "AnswerOption [optionID=" + optionID + ", question=" + question + ", optionText=" + optionText
				+ ", isCorrect=" + isCorrect + "]";
	}
	

}
