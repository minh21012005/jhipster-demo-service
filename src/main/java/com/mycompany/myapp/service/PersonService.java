package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Person;
import com.mycompany.myapp.domain.dto.PersonDTO;
import com.mycompany.myapp.repository.PersonRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDTO> findAll() {
        return personRepository
            .findAll()
            .stream()
            .map(person -> {
                return new PersonDTO(person.getId(), person.getName(), person.getEmail());
            })
            .toList();
    }

    public void save(Person person) {
        personRepository.save(person);
    }
}
