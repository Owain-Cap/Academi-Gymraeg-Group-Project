package uk.ac.bangor.cs.group2.academicymraeg.repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.group2.academicymraeg.models.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
	
	List<Test> findByUsernameOrderByCreatedAtDesc(String username);
	
	List<Test> findByUsernameAndStatusOrderByCreatedAtDesc(String username, Test.TestStatus status);
	
	boolean existsByUsernameAndStatus(String username, Test.TestStatus status);
	
	Optional<Test> findByUsernameAndStatus(String username, Test.TestStatus status);
}
