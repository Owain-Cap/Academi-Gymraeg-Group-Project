package uk.ac.bangor.cs.group2.academicymraeg.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Noun {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nounId;
	private String english;
	private String welsh;

	public enum Gender {
		MASCULINE, FEMININE
	}

	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	private String createdByUsername;
	private LocalDateTime createdAt;
	
	protected Noun() {
	}
	
	public Noun(Long nounId, String english, String welsh, Gender gender, String createdByUsername,
			LocalDateTime createdAt) {
		super();
		this.nounId = nounId;
		this.english = english;
		this.welsh = welsh;
		this.gender = gender;
		this.createdByUsername = createdByUsername;
		this.createdAt = createdAt;
	}

	public long getNounId() {
		return nounId;
	}
	
	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getWelsh() {
		return welsh;
	}

	public void setWelsh(String welsh) {
		this.welsh = welsh;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getCreatedByUsername() {
		return createdByUsername;
	}

	public void setCreatedByUsername(String createdByUsername) {
		this.createdByUsername = createdByUsername;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	

	
	
}