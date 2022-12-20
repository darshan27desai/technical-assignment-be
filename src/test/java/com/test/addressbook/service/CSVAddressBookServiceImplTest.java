package com.test.addressbook.service;

import com.test.addressbook.model.Person;
import com.test.addressbook.repository.impl.CSVAddressBookRepoImpl;
import com.test.addressbook.service.impl.CSVAddressBookServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class CSVAddressBookServiceImplTest {

    @Mock
    CSVAddressBookRepoImpl csvAddressBookRepo;

    @InjectMocks
    CSVAddressBookServiceImpl csvAddressBookService;

    @Test
    public void testGetAddressBook(){
        List<Person> personList  = new ArrayList<>();

        Person person1 = Person.builder().name("John Doe")
                .dateOfBirth(LocalDate.now()).gender(Person.GenderEnum.MALE).build();

        Person person2 = Person.builder().name("Jane Doe")
                .dateOfBirth(LocalDate.now()).gender(Person.GenderEnum.FEMALE).build();

        personList.add(person1);
        personList.add(person2);

        when(csvAddressBookRepo.getListOfPerson()).thenReturn(personList);

        assertEquals(personList, csvAddressBookService.getAddressBook());
    }

    @Test
    public void testGetAddressBookWithOnlyGenderFilter(){
        List<Person> personList  = new ArrayList<>();

        Person person1 = Person.builder().name("John Doe")
                .dateOfBirth(LocalDate.now()).gender(Person.GenderEnum.MALE).build();

        Person person2 = Person.builder().name("Jane Doe")
                .dateOfBirth(LocalDate.now()).gender(Person.GenderEnum.FEMALE).build();

        personList.add(person1);
        personList.add(person2);

        List<Person> resultantList = new ArrayList<>();
        resultantList.add(person1);

        when(csvAddressBookRepo.getListOfPerson()).thenReturn(personList);

        assertEquals(resultantList, csvAddressBookService.getAddressBook("male", "all"));
    }

    @Test
    public void testGetAddressBookWithOnlyAgeFilter(){
        List<Person> personList  = new ArrayList<>();

        Person person1 = Person.builder().name("John Doe")
                .dateOfBirth(LocalDate.of(1999, 12, 01)).gender(Person.GenderEnum.MALE).build();

        Person person2 = Person.builder().name("Jack Doe")
                .dateOfBirth(LocalDate.of(1970, 11, 01)).gender(Person.GenderEnum.MALE).build();

        Person person3 = Person.builder().name("Jane Doe")
                .dateOfBirth(LocalDate.of(1999, 11, 01)).gender(Person.GenderEnum.FEMALE).build();

        personList.add(person1);
        personList.add(person2);
        personList.add(person3);

        List<Person> resultantList = new ArrayList<>();
        resultantList.add(person2);

        when(csvAddressBookRepo.getListOfPerson()).thenReturn(personList);

        assertEquals(resultantList, csvAddressBookService.getAddressBook("all", "oldest"));
    }

    @Test
    public void testGetAddressBookWithAgeFilterAsOlderAndGenderFilterAsMale(){
        List<Person> personList  = new ArrayList<>();

        Person person1 = Person.builder().name("John Doe")
                .dateOfBirth(LocalDate.of(1999, 12, 01)).gender(Person.GenderEnum.MALE).build();

        Person person2 = Person.builder().name("Jack Doe")
                .dateOfBirth(LocalDate.of(1970, 11, 01)).gender(Person.GenderEnum.MALE).build();

        Person person3 = Person.builder().name("Jane Doe")
                .dateOfBirth(LocalDate.of(1999, 11, 01)).gender(Person.GenderEnum.FEMALE).build();

        personList.add(person1);
        personList.add(person2);
        personList.add(person3);

        List<Person> resultantList = new ArrayList<>();
        resultantList.add(person2);

        when(csvAddressBookRepo.getListOfPerson()).thenReturn(personList);

        assertEquals(resultantList, csvAddressBookService.getAddressBook("male", "oldest"));
    }

    @Test
    public void testGetAddressBookWithAgeFilterAsYoungerAndGenderFilterAsFemale(){
        List<Person> personList  = new ArrayList<>();

        Person person1 = Person.builder().name("John Doe")
                .dateOfBirth(LocalDate.of(1999, 12, 01)).gender(Person.GenderEnum.MALE).build();

        Person person2 = Person.builder().name("Jack Doe")
                .dateOfBirth(LocalDate.of(1970, 11, 01)).gender(Person.GenderEnum.MALE).build();

        Person person3 = Person.builder().name("Jane Doe")
                .dateOfBirth(LocalDate.of(1999, 11, 01)).gender(Person.GenderEnum.FEMALE).build();

        personList.add(person1);
        personList.add(person2);
        personList.add(person3);

        List<Person> resultantList = new ArrayList<>();
        resultantList.add(person3);

        when(csvAddressBookRepo.getListOfPerson()).thenReturn(personList);

        assertEquals(resultantList, csvAddressBookService.getAddressBook("female", "youngest"));
    }
}
