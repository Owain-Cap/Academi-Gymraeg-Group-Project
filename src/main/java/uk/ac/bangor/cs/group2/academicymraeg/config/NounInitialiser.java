package uk.ac.bangor.cs.group2.academicymraeg.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.ac.bangor.cs.group2.academicymraeg.models.Noun;
import uk.ac.bangor.cs.group2.academicymraeg.models.Noun.Gender;
import uk.ac.bangor.cs.group2.academicymraeg.repository.NounRepository;

/**
 * Configuration class used to insert nouns into the database.
 *
 * this runs when the application is started. 
 * It checks to see if the noun table is empty,
 * if the noun table is empty then the it will insert a list 
 * of Welsh nouns with the English meaning and gender
 */

@Configuration
public class NounInitialiser {

	/**
	 * creates a CommandLineRunner that runs when the application is started
	 * 
	 * if the noun table is empty, the nouns will be created and added to the database
	 * this will allow for the student to be take the test straight away rather then having the instructors
	 * having to fill in every noun by hand
	 * 
	 * @param nounRepository repository used to access and save the nouns
	 * @return a CommandLineRunner that inserts the nouns when the table is empty
	 */
	
	@Bean
	public
    CommandLineRunner initNouns(NounRepository nounRepository) {
        return args -> {
        	
        	//Makes sure that the table is empty
        	if (nounRepository.count()==0) {
        		List<Noun>nouns = List.of(
        				
        				//this is the data that will be inserted into the table if table is empty
        				
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
	
	
	/**
	 * 
	 * creates a new noun object with the Welsh,English and gender
	 * located above
	 * 
	 * the createdAt field is set to be the current date and time
	 * the createdByUsername is set to APPLICATION SYSTEM as the nouns
	 * are going to be inserted automatically when the application starts
	 * 
	 * 
	 * @param english the English mean ing of the noun
	 * @param welsh the Welsh  meaning of the noun 
	 * @param gender the gender of the noun
	 * @return a new Noun object ready to saved
	 */
	
	
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
