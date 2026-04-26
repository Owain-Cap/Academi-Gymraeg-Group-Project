package uk.ac.bangor.cs.group2.academicymraeg.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * entity class representing a noun in the database.
 *
 * each noun has an ID, an English word, a Welsh word, gender, the username of
 * the person who created it, and the date and time it was created.
 */

@Entity
public class Noun {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nounId;
	
	@Column(unique = true, nullable = false)
	private String english;
	
	@Column(unique = true, nullable = false)
	private String welsh;

	/**
	 * represents the gender of a Welsh noun.
	 */
	public enum Gender {
		MASCULINE, FEMININE
	}

	/**
	 * stores the gender as text in the database instead of a number.
	 */
	@Enumerated(EnumType.STRING)
	private Gender gender;

	/**
	 * this is who has created the noun
	 */
	private String createdByUsername;

	/**
	 * when then noun was created
	 */
	private LocalDateTime createdAt;

	/**
	 * Default constructor required by JPA.
	 */
	public Noun() {

	}

	/**
	 * constructor used to create a noun object with all fields.
	 *
	 * @param nounId            the ID of the noun
	 * @param english           the English version of the noun
	 * @param welsh             the Welsh version of the noun
	 * @param gender            the gender of the Welsh noun
	 * @param createdByUsername the username of the person who created the noun
	 * @param createdAt         the date and time the noun was created
	 */

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

	/**
	 * gets the noun ID.
	 *
	 * @return the noun ID
	 */
	public Long getNounId() {
		return nounId;
	}

	/**
	 * sets the noun ID.
	 *
	 * @param nounId the noun ID
	 */
	public void setNounId(Long nounId) {
	    this.nounId = nounId;
	}
	
	/**
	 * gets the english version of the noun.
	 *
	 * @return the english noun
	 */
	public String getEnglish() {
		return english;
	}

	/**
	 * sets the English version of the noun.
	 *
	 * @param english the English noun
	 */
	public void setEnglish(String english) {
		this.english = english;
	}

	/**
	 * gets the Welsh version of the noun.
	 *
	 * @return the Welsh noun
	 */
	public String getWelsh() {
		return welsh;
	}

	/**
	 * sets the Welsh version of the noun.
	 *
	 * @param welsh the Welsh noun
	 */
	public void setWelsh(String welsh) {
		this.welsh = welsh;
	}

	/**
	 * gets the gender of the noun.
	 *
	 * @return the noun gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * sets the gender of the noun.
	 *
	 * @param gender the noun gender
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * gets the username of the person who created the noun.
	 *
	 * @return the creator username
	 */
	public String getCreatedByUsername() {
		return createdByUsername;
	}

	/**
	 * sets the username of the person who created the noun.
	 *
	 * @param createdByUsername the creator username
	 */
	public void setCreatedByUsername(String createdByUsername) {
		this.createdByUsername = createdByUsername;
	}

	/**
	 * gets the date and time the noun was created.
	 *
	 * @return the creation date and time
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * sets the date and time the noun was created.
	 *
	 * @param createdAt the creation date and time
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * returns a text version of the noun object.
	 *
	 * @return noun details as a string
	 */
	@Override
	public String toString() {
		return "Noun [nounId=" + nounId + ", english=" + english + ", welsh=" + welsh + ", gender=" + gender
				+ ", createdByUsername=" + createdByUsername + ", createdAt=" + createdAt + "]";
	}
}