package uk.ac.bangor.cs.group2.academicymraeg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
/*
 * JPA Repository library creates SQL queried without us needing to write them out
 * Supports
 * save()
 * findById()
 * findAll()
 * delete()
 * count()
 * existsById()
 * documentation here: https://spring.io/guides/gs/accessing-data-jpa
 * */

import uk.ac.bangor.cs.group2.academicymraeg.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	//creates a query to retrieve matching usernames from the database
	//optional tag added to prevent unregistered usernames crashing the program
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
}
