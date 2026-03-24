package uk.ac.bangor.cs.group2.academicymraeg.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.ac.bangor.cs.group2.academicymraeg.repository.NounRepository;

@Configuration
public class NounInitialiser {

    @Bean
    CommandLineRunner initNouns(NounRepository nounRepository) {
        return args -> {
        	
        };
    }
}