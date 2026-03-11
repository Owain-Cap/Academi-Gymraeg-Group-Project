/*
package uk.ac.bangor.cs.group2.academicymraeg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class NounBackup{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long backupID;
	
	@OneToOne
	@JoinColumn(name = "questionID")
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
*/
