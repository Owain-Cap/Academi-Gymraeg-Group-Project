package uk.ac.bangor.cs.group2.academicymraeg;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class Test implements SQLObject<Test>{

	private long testID;
	private User Student;
	private List<String> answer;
	private LocalDateTime startedAt;
	private LocalDateTime submittedAt;
	private int score;


	public Test() {

	}

	public Test(long testID, User student, List<String> answer, LocalDateTime startedAt, LocalDateTime submittedAt,
			int score) {
		super();
		this.testID = testID;
		Student = student;
		this.answer = answer;
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

	public List<String> getAnswer() {
		return answer;
	}

	public void setAnswer(List<String> answer) {
		this.answer = answer;
	}

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
	public Collection<Test> fromResultSet(ResultSet rs) {
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
		return "Test [testID=" + testID + ", Student=" + Student + ", answer=" + answer + ", startedAt=" + startedAt
				+ ", submittedAt=" + submittedAt + ", score=" + score + "]";
	}
	
	
}
