package com.test.addressbook.repository;

import com.test.addressbook.model.Person;
import com.test.addressbook.model.exception.PersonNotFoundException;
import com.test.addressbook.repository.impl.CSVAddressBookRepoImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(SpringExtension.class)
public class CSVAddressBookRepoImplTest {
    @InjectMocks
    private CSVAddressBookRepoImpl csvAddressBookRepo;
    @BeforeEach
    public void init() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:/csv/addressbook");
        ReflectionTestUtils.setField(csvAddressBookRepo, "pathToCSV", resource);
    }

    @Test
    public void testGetListOfPerson(){

        List<Person> personList  = new ArrayList<>();

        Person person1 = Person.builder().name("John Doe")
                .dateOfBirth(LocalDate.of(1999, 12, 01)).gender(Person.GenderEnum.MALE).build();

        Person person2 = Person.builder().name("Jane Doe")
                .dateOfBirth(LocalDate.of(1970, 11, 01)).gender(Person.GenderEnum.FEMALE).build();

        personList.add(person1);
        personList.add(person2);

        List<Person> result = csvAddressBookRepo.getListOfPerson();
        assertEquals(personList, result);
    }


    @Test
    public void testGetPersonByName(){

        Person person= Person.builder().name("John Doe")
                .dateOfBirth(LocalDate.of(1999, 12, 01)).gender(Person.GenderEnum.MALE).build();

        Person result = csvAddressBookRepo.getPersonByName("John Doe");
        assertEquals(person, result);
    }


    @Test
    public void testGetPersonByNameNegativeScenario(){

        PersonNotFoundException personNotFoundException = assertThrows(PersonNotFoundException.class,
                () -> csvAddressBookRepo.getPersonByName("Jackie Doe"));

        assertEquals("Person not Found", personNotFoundException.getMessage());
    }

}
