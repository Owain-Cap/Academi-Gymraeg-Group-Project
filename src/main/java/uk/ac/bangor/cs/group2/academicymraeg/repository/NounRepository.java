package uk.ac.bangor.cs.group2.academicymraeg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.group2.academicymraeg.models.Noun;

@Repository
public interface NounRepository extends JpaRepository<Noun, Long> {
	//shows the results in a list 
	List<Noun> findByEnglishContainingIgnoreCaseOrWelshContainingIgnoreCase(String english, String welsh);
}