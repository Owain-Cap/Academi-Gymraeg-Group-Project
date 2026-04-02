package uk.ac.bangor.cs.group2.academicymraeg.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.ac.bangor.cs.group2.academicymraeg.models.Noun;
import uk.ac.bangor.cs.group2.academicymraeg.models.Noun.Gender;
import uk.ac.bangor.cs.group2.academicymraeg.repository.NounRepository;

@Configuration
public class NounInitialiser {


	@Bean
	public
    CommandLineRunner initNouns(NounRepository nounRepository) {
        return args -> {
        	
        	//Makes sure that the table is empty and then this is the data that would be inserted into the table
        	if (nounRepository.count()==0) {
        		List<Noun>nouns = List.of(
        				createNoun("cat","cath",Gender.FEMININE),
        				createNoun("potato","tatws",Gender.FEMININE),
        				createNoun("computer","cyfrifiadur",Gender.MASCULINE),
        				createNoun("sandwich","brechdan",Gender.FEMININE),
        				createNoun("student(male)","myfyrwr",Gender.MASCULINE),
        				createNoun("student(female)","myfyrwraig",Gender.FEMININE),
        				createNoun("book","llyfr",Gender.MASCULINE),
        				createNoun("table","bwrdd",Gender.MASCULINE),
        				createNoun("car","car",Gender.MASCULINE),
        				createNoun("dog","ci",Gender.MASCULINE),
        				createNoun("teacher(male)","athro",Gender.MASCULINE),
        				createNoun("teacher(female)","athrawes",Gender.FEMININE),
        				createNoun("pencil","pensil",Gender.MASCULINE),
        				createNoun("pen","beiro",Gender.MASCULINE),
        				createNoun("paper","papur",Gender.MASCULINE),
        				createNoun("mathematics","mathemateg",Gender.MASCULINE),
        				createNoun("geography","daearyddiaeth",Gender.FEMININE),
        				createNoun("music","cerddoriaeth",Gender.FEMININE),
        				createNoun("house","tŷ",Gender.MASCULINE),
        				createNoun("geography","daearyddiaeth",Gender.FEMININE),
        				createNoun("history","hanes",Gender.MASCULINE),
        				createNoun("canteen","ffreutur",Gender.FEMININE),
        				createNoun("ruler","mesurydd",Gender.MASCULINE),
        				createNoun("glue","glud",Gender.MASCULINE),
        				createNoun("exam","arholiad",Gender.MASCULINE),
        				createNoun("scissors","siswrn",Gender.FEMININE),
        				createNoun("school","ysgol",Gender.FEMININE),
        				createNoun("board","bwrdd",Gender.MASCULINE),
        				createNoun("test","prawf",Gender.MASCULINE),
        				createNoun("lesson","gwers",Gender.FEMININE),
        				createNoun("reading","darllen",Gender.FEMININE),
        				createNoun("bag","bag",Gender.MASCULINE),
        				createNoun("project","prosiect",Gender.FEMININE),
        				createNoun("hall","neuadd",Gender.MASCULINE)
        				);
        		//this saves the nouns
        		nounRepository.saveAll(nouns);
        		//Prints a message that the data is saved 
        		System.out.println("Data inserted into the database");
        	}
        };
    }
	
	//this helps the methods to get saved into the database
	private Noun createNoun(String english, String welsh, Gender gender) {
		Noun noun = new Noun();
		noun.setEnglish(english);
		noun.setWelsh(welsh);
		noun.setGender(gender);
		noun.setCreatedAt(LocalDateTime.now());
		noun.setCreatedByUsername("APPLICATION SYSTEM"); //This is for the user name will be for the added nouns at the start (NAME can be changed)
		return noun;
	};
}