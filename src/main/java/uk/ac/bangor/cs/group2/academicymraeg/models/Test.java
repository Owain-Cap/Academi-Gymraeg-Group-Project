package uk.ac.bangor.cs.group2.academicymraeg.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Test{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long testID;
	
	@ManyToOne
	@JoinColumn(name = "userID")
	private User Student;
	//private List<String> answer;
	private LocalDateTime startedAt;
	private LocalDateTime submittedAt;
	private int score;


	public Test() {

	}

	public Test(long testID, User student, LocalDateTime startedAt, LocalDateTime submittedAt,
			int score) {
		super();
		this.testID = testID;
		Student = student;
		//this.answer = answer;
		this.startedAt = startedAt;
		this.submittedAt = submittedAt;
		this.score = score;
	}

	public User getStudent() {
		return Student;
	}

	public void setStudent(User student) {
		Student = student;
	}

	/*
	public List<String> getAnswer() {
		return answer;
	}

	public void setAnswer(List<String> answer) {
		this.answer = answer;
	}
	*/

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
	}

	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getTestID() {
		return testID;
	}

	public boolean isSubmitted(){
		return submittedAt !=null;

	}

	public void calculateScore() {
		// going to be for calculating the score

	}


	@Override
	public String toString() {
		return "Test [testID=" + testID + ", Student=" + Student + ", startedAt=" + startedAt
				+ ", submittedAt=" + submittedAt + ", score=" + score + "]";
	}
	
}
