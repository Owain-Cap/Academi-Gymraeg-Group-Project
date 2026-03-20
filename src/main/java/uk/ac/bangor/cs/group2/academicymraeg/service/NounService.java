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

	public List<Noun> getAllNouns() {
		return nounRepository.findAll();
	}

	public Noun getNounById(Long id) {
		return nounRepository.findById(id).orElse(null);
	}

	public Noun saveNoun(Noun noun) {
		return nounRepository.save(noun);
	}

	public void deleteNoun(Long id) {
		nounRepository.deleteById(id);
	}
	public List<Noun> searchNouns(String search) {
	    return nounRepository.findByEnglishContainingIgnoreCaseOrWelshContainingIgnoreCase(search, search);
	}
}