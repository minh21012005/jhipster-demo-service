package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Person;
import com.mycompany.myapp.domain.dto.PersonDTO;
import com.mycompany.myapp.service.PersonService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PersonResource {

    private final PersonService personService;

    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<PersonDTO> getAllPeople() {
        return personService.findAll();
    }

    @PostMapping
    public void createPerson() {
        Person person = new Person();
        person.setEmail("abc");
        person.setName("xyz");
        personService.save(person);
    }
}
