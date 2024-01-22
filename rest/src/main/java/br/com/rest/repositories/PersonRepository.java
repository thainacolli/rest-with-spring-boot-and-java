package br.com.rest.repositories;


import br.com.rest.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
