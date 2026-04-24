package uk.ac.bangor.cs.group2.academicymraeg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.bangor.cs.group2.academicymraeg.models.Noun;
import uk.ac.bangor.cs.group2.academicymraeg.repository.NounRepository;

@Service
public class NounService {

	private final NounRepository nounRepository;

	@Autowired
	public NounService(NounRepository nounRepository) {
		this.nounRepository = nounRepository;
	}

	// gets all the nouns
	public List<Noun> getAllNouns() {
		return nounRepository.findAll();
	}

	// used for the edit function which gets the noun by the ID
	public Noun getNounById(Long id) {
		return nounRepository.findById(id).orElse(null);
	}

	/**
	 * Checks whether a noun already exists in the database by comparing either the
	 * English word or the Welsh word.
	 * 
	 * @param english the English word to check
	 * @param welsh   the Welsh word to check
	 * @return true if either the English or Welsh word already exists; false
	 *         otherwise
	 */
	public boolean nounExists(String english, String welsh) {
		return nounRepository.existsByEnglishIgnoreCase(english) || nounRepository.existsByWelshIgnoreCase(welsh);
	}

	// save
	public Noun saveNoun(Noun noun) {
		return nounRepository.save(noun);
	}

	// deletes by NounID
	public void deleteNoun(Long id) {
		nounRepository.deleteById(id);
	}

	// shows the list for the search function
	public List<Noun> searchNouns(String search) {
		return nounRepository.findByEnglishContainingIgnoreCaseOrWelshContainingIgnoreCase(search, search);
	}

	public boolean isValid(String word) {
		return word.matches("[a-zA-ZáéíóúŵẃỳÁÉÍÓÚŴẂỲ]+");
	}
}