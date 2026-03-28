package uk.ac.bangor.cs.group2.academicymraeg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import uk.ac.bangor.cs.group2.academicymraeg.service.NounService;

@SpringBootTest
class NounWordTest {

	@Autowired
    private NounService nounService;

    @Test
    void contextLoads() {
    }

    @Test
    void testValidWord() {
        assertTrue(nounService.isValid("school"));
    }

    @Test
    void testInvalidWord() {
        assertFalse(nounService.isValid("school123"));
    }
}
