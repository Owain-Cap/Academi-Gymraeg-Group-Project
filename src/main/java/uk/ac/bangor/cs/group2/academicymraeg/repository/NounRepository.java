package uk.ac.bangor.cs.group2.academicymraeg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.bangor.cs.group2.academicymraeg.models.Noun;

@Repository
public interface NounRepository extends JpaRepository<Noun, Long> {
}