package uk.ac.bangor.cs.group2.academicymraeg;

import java.sql.ResultSet;
import java.util.Collection;

public class NounBackup{

	private long backupID;
	private Question question;
	private String englishWord;
	private String welshWord;
	
	//I'm assuming we are talking about the noun gender
	private Noun gender;
	
	
	public NounBackup() {
		
	}

	public NounBackup(long backupID, Question question, String englishWord, String welshWord, Noun gender) {
		super();
		this.backupID = backupID;
		this.question = question;
		this.englishWord = englishWord;
		this.welshWord = welshWord;
		this.gender = gender;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getEnglishWord() {
		return englishWord;
	}

	public void setEnglishWord(String englishWord) {
		this.englishWord = englishWord;
	}

	public String getWelshWord() {
		return welshWord;
	}

	public void setWelshWord(String welshWord) {
		this.welshWord = welshWord;
	}

	public Noun getGender() {
		return gender;
	}

	public void setGender(Noun gender) {
		this.gender = gender;
	}

	public long getBackupID() {
		return backupID;
	}

	@Override
	public String toString() {
		return "NounBackup [backupID=" + backupID + ", question=" + question + ", englishWord=" + englishWord
				+ ", welshWord=" + welshWord + ", gender=" + gender + "]";
	}
	
}
