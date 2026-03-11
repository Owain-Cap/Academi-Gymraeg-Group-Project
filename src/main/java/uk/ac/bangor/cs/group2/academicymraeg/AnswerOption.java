package uk.ac.bangor.cs.group2.academicymraeg;

public class AnswerOption{
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
	public String toString() {
		return "AnswerOption [optionID=" + optionID + ", question=" + question + ", optionText=" + optionText
				+ ", isCorrect=" + isCorrect + "]";
	}
	
}
