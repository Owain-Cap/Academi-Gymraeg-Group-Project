package uk.ac.bangor.cs.group2.academicymraeg.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Test{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long testId;
	private String username;
	private int result;
	private LocalDateTime createdAt;
	
	public Test(long testId, String username, int result, LocalDateTime createdAt) {
		super();
		this.testId = testId;
		this.username = username;
		this.result = result;
		this.createdAt = createdAt;
	}

	public long getTestId() {
		return testId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Test [testId=" + testId + ", username=" + username + ", result=" + result + ", createdAt=" + createdAt
				+ "]";
	}
	

	
	
}
	


