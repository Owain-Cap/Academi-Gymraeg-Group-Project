package uk.ac.bangor.cs.group2.academicymraeg;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import uk.ac.bangor.cs.group2.academicymraeg.config.NounInitialiser;
import uk.ac.bangor.cs.group2.academicymraeg.repository.NounRepository;

	
	@SpringBootTest
	class NounSaving {

	@Autowired
	private NounInitialiser nounInitialiser;
	
	@Autowired
	private NounRepository nounRepository;
	
	@Test
	void test() {
		
	}
	@Test
	void testWhenEmpty() throws Exception{
		
		nounRepository.deleteAll();
		
		nounInitialiser.initNouns(nounRepository).run();
		
		assertTrue(nounRepository.count() > 0);
		
	}

	private void assertTrue(boolean b) {
		// TODO Auto-generated method stub
		
	}
	@Test
    void noDuplicateDataInsert() throws Exception {

        
        nounRepository.deleteAll();
        nounInitialiser.initNouns(nounRepository).run();

        long countBefore = nounRepository.count();

       
        nounInitialiser.initNouns(nounRepository).run();

        long countAfter = nounRepository.count();

       
		assertEquals(countBefore, countAfter);
    }
	private void assertEquals(long countBefore, long countAfter) {
		// TODO Auto-generated method stub
		
	}
}
