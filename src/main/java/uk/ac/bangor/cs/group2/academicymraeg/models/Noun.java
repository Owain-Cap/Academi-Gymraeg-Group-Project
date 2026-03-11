package uk.ac.bangor.cs.group2.academicymraeg;

import java.time.LocalDateTime;

public class Noun {
	private long nounID;
	private String englishWord;
	private String welshWord;

	public enum Gender {
		MASCULINE, FEMININE
	}

	private Gender gender;
	private User addedBy;
	private LocalDateTime createdAt;
	private boolean active;

	public Noun() {

	}

	public Noun(long nounID, String englishWord, String welshWord, Gender gender, User addedBy, LocalDateTime createdAt,
			boolean active) {
		super();
		this.nounID = nounID;
		this.englishWord = englishWord;
		this.welshWord = welshWord;
		this.gender = gender;
		this.addedBy = addedBy;
		this.createdAt = createdAt;
		this.active = active;
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	// unsure if we needed this so we can always delete
	public User getAddedBy() {
		return addedBy;
	}

	// unsure if we needed this so we can always delete
	public void setAddedBy(User addedBy) {
		this.addedBy = addedBy;
	}

	// unsure if we needed this so we can always delete
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	// unsure if we needed this so we can always delete
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getNounID() {
		return nounID;
	}


	@Override
	public String toString() {
		return "Noun [nounID=" + nounID + ", englishWord=" + englishWord + ", welshWord=" + welshWord + ", gender="
				+ gender + ", addedBy=" + addedBy + ", createdAt=" + createdAt + ", active=" + active + "]";
	}
}
