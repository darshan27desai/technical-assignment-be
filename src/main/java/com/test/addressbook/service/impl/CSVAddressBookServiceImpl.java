package com.test.addressbook.service.impl;

import com.test.addressbook.model.Person;
import com.test.addressbook.repository.AddressBookRepo;
import com.test.addressbook.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
@Service
@Qualifier("csvAddressBookService")
public class CSVAddressBookServiceImpl implements AddressBookService {
    private AddressBookRepo addressBookRepo;
    public CSVAddressBookServiceImpl(@Autowired
                                     @Qualifier("csvAddressBookRepo")
                                     final AddressBookRepo csvAddressBookRepo){
        this.addressBookRepo = csvAddressBookRepo;
    }

    public List<Person> getAddressBook(){
        return addressBookRepo.getListOfPerson();
    }

    @Override
    public List<Person> getAddressBook(final String genderFilter, final String ageFilter) {
        List<Person> listOfPerson = this.getAddressBook();

        listOfPerson = filterPersonAsPerSelectedGender(genderFilter, listOfPerson);

        return filterPersonAsPerSelectedAge(ageFilter, listOfPerson);
    }


    private List<Person> filterPersonAsPerSelectedGender(final String genderFilter, List<Person> listOfAllPerson)
    {

        Predicate<String> checkMaleFilter = str -> str.equalsIgnoreCase("male");
        Predicate<String> checkFemaleFilter = str -> str.equalsIgnoreCase("female");

        List<Person> resultantListOfPerson = listOfAllPerson;
        if((checkMaleFilter.or(checkFemaleFilter)).test(genderFilter)){
            resultantListOfPerson =  listOfAllPerson.stream()
                    .filter(person -> person.getGender().getValue().equalsIgnoreCase(genderFilter))
                    .collect(Collectors.toList());
        }
        return resultantListOfPerson;
    }


    private List<Person> filterPersonAsPerSelectedAge(final String ageFilter, List<Person> listOfPerson)
    {
        Predicate<String> checkNoAgeFilter = str -> str.equalsIgnoreCase("all");
        Predicate<String> checkOldestFilter = str -> str.equalsIgnoreCase("oldest");
        Predicate<String> checkYoungestFilter = str -> str.equalsIgnoreCase("youngest");
        List<Person> resultantListOfPerson = listOfPerson;


        if(checkNoAgeFilter.test(ageFilter)){
            return resultantListOfPerson;
        }else if(checkOldestFilter.test(ageFilter)){
            resultantListOfPerson.sort(Comparator.comparing(Person::getDateOfBirth));

        }else if(checkYoungestFilter.test(ageFilter)){
            resultantListOfPerson.sort(Comparator.comparing(Person::getDateOfBirth).reversed());
        }

        return Collections.singletonList(resultantListOfPerson
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid List of Person")));
    }

}
