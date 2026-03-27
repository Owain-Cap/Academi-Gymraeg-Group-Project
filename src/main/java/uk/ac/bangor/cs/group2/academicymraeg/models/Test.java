package uk.ac.bangor.cs.group2.academicymraeg.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Test{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long testId;
	private String username;
	private int result;
	private LocalDateTime createdAt;
	
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestQuestions> questions;
	
	public Test(long testId, String username, int result, LocalDateTime createdAt) {
		super();
		this.testId = testId;
		this.username = username;
		this.result = result;
		this.createdAt = createdAt;
	}

	

	public Test() {
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
	
    public List<TestQuestions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<TestQuestions> questions) {
        this.questions = questions;
    }

	@Override
	public String toString() {
		return "Test [testId=" + testId + ", username=" + username + ", result=" + result + ", createdAt=" + createdAt
				+ "]";
	}
	

	
	
}
	


