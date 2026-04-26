package uk.ac.bangor.cs.group2.academicymraeg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.bangor.cs.group2.academicymraeg.models.Noun;
import uk.ac.bangor.cs.group2.academicymraeg.repository.NounRepository;

/**
 * service class responsible for handling noun logic.
 *
 * acts as a middle layer between the controller and repository.
 */
@Service
public class NounService {

	private final NounRepository nounRepository;

	/**
	 * constructor for nounservice.
	 *
	 * @param nounRepository repository used to access noun data
	 */
	@Autowired
	public NounService(NounRepository nounRepository) {
		this.nounRepository = nounRepository;
	}

	/**
	 * retrieves all nouns from the database.
	 *
	 * @return list of all nouns
	 */
	public List<Noun> getAllNouns() {
		return nounRepository.findAll();
	}

	/**
	 * retrieves a noun by its id.
	 *
	 * @param id the id of the noun
	 * @return the noun if found, otherwise null
	 */
	public Noun getNounById(Long id) {
		return nounRepository.findById(id).orElse(null);
	}

	/**
	 * saves a noun to the database.
	 *
	 * used for both creating and updating nouns.
	 *
	 * @param noun the noun object to save
	 * @return the saved noun
	 */
	public Noun saveNoun(Noun noun) {
		return nounRepository.save(noun);
	}

	/**
	 * deletes a noun using its id.
	 *
	 * @param id the id of the noun to delete
	 */
	public void deleteNoun(Long id) {
		nounRepository.deleteById(id);
	}

	/**
	 * searches for nouns based on English or Welsh values.
	 *
	 * the search is case insensitive.
	 *
	 * @param search the search term entered by the user
	 * @return list of matching nouns
	 */
	public List<Noun> searchNouns(String search) {
		return nounRepository.findByEnglishContainingIgnoreCaseOrWelshContainingIgnoreCase(search, search);
	}

	/**
	 * checks if a word only contains valid letters, including Welsh characters.
	 *
	 * @param word the word to validate
	 * @return true if valid, otherwise false
	 */
	public boolean isValid(String word) {
		return word.matches("[a-zA-ZáéíóúŵẃỳÁÉÍÓÚŴẂỲ]+");
	}
}