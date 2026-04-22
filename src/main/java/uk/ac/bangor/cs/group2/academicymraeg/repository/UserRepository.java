package uk.ac.bangor.cs.group2.academicymraeg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * JPA Repository library creates SQL queries without us needing to write them out.
 * Supports standard CRUD operations including:
 * save()
 * findById()
 * findAll()
 * delete()
 * count()
 * existsById()
 * 
 * Documentation: https://spring.io/guides/gs/accessing-data-jpa
 */

import uk.ac.bangor.cs.group2.academicymraeg.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Creates a query to retrieve matching usernames from the database.
	 * An Optional wrapper is used to handle unregistered usernames without crashing the program.
	 *
	 * @param username The username to search for.
	 * @return An Optional containing the User if found, otherwise empty.
	 */
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
}