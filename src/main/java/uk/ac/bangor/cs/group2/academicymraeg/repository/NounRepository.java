package uk.ac.bangor.cs.group2.academicymraeg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.group2.academicymraeg.models.Noun;

/**
 * repository used to access noun data from the database.
 *
 * extends jparepository to provide built-in methods such as findall,
 * findbyid, save and deletebyid.
 */
@Repository
public interface NounRepository extends JpaRepository<Noun, Long> {

	/**
	 * finds nouns where the English or Welsh value contains the search text.
	 *
	 * the search is case insensitive.
	 *
	 * @param english search value for the English word
	 * @param welsh search value for the Welsh word
	 * @return list of matching nouns
	 */
	List<Noun> findByEnglishContainingIgnoreCaseOrWelshContainingIgnoreCase(String english, String welsh);
}