package uk.ac.bangor.cs.group2.academicymraeg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.bangor.cs.group2.academicymraeg.models.Test;
import uk.ac.bangor.cs.group2.academicymraeg.models.TestQuestions;

public interface TestQuestionRepository extends JpaRepository<TestQuestions, Long> {
	List<TestQuestions> findByTestOrderByPositionAsc(Test test);

}
