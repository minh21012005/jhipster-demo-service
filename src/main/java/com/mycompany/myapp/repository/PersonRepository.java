package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // custom query nếu cần
}
