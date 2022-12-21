package com.test.addressbook.service.impl;

import com.test.addressbook.model.DifferenceInAge;
import com.test.addressbook.model.Person;
import com.test.addressbook.model.exception.InvalidCriteriaException;
import com.test.addressbook.repository.AddressBookRepo;
import com.test.addressbook.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Slf4j
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
            return listOfPerson;
        }else if(checkOldestFilter.test(ageFilter)){
            resultantListOfPerson.sort(Comparator.comparing(Person::getDateOfBirth));

        }else if(checkYoungestFilter.test(ageFilter)){
            resultantListOfPerson.sort(Comparator.comparing(Person::getDateOfBirth).reversed());
        }
        return Collections.singletonList(resultantListOfPerson
                .stream()
                .findFirst()
                .orElseThrow(() -> {
                   log.error("Person list seems to be null or empty: {}", resultantListOfPerson.size());
                   return new IllegalArgumentException("Invalid List of Person");
                }));
    }

    @Override
    public DifferenceInAge getDifferenceOfAge(String nameOfPersonToCompare, String nameOfSelectedPerson, String unit, String criteria) {
        Person personToCompare = this.getPersonByName(nameOfPersonToCompare);
        Person selectedPerson = this.getPersonByName(nameOfSelectedPerson);

        log.debug("Person to compare: {} and selected person: {}", nameOfPersonToCompare, nameOfPersonToCompare);

        Boolean isAgeCriteriaCorrect = validateCriteria(personToCompare, selectedPerson, criteria);

        assert isAgeCriteriaCorrect.equals(Boolean.TRUE);

        Long ageDifference = calculateAgeDifferenceBasedOnCriteria(personToCompare, selectedPerson, unit, criteria);

        return DifferenceInAge.builder().count(ageDifference).unit(DifferenceInAge.UnitEnum.valueOf(unit.toUpperCase()))
                .criteria(DifferenceInAge.CriteriaEnum.valueOf(criteria.toUpperCase())).build();

    }

    private Person getPersonByName(String name){
        return addressBookRepo.getPersonByName(name);
    }

    private boolean validateCriteria(Person personToCompare, Person selectedPerson,  String criteria){
        Predicate<String> older = str -> str.equalsIgnoreCase("older");

        if(older.test(criteria)){
            Optional.of(personToCompare.getDateOfBirth())
                    .filter(personDOB -> personDOB.isBefore(selectedPerson.getDateOfBirth()))
                    .orElseThrow(() -> {
                        log.error("Invalid age comparison criteria selected: {}", criteria);
                        return new InvalidCriteriaException("Invalid age comparison criteria selected: "+criteria);
                    });
        }
        return true;
    }

    /***
     * This method is used to get age difference based on older age criteria as filter.
     * This can further be extended to include younger as age criteria filter.
     * @param personToCompare
     * @param selectedPerson
     * @param unit
     * @param criteria
     * @return
     */
    private Long calculateAgeDifferenceBasedOnCriteria(Person personToCompare, Person selectedPerson, String unit, String criteria){
        if(criteria.equalsIgnoreCase("older")){
            return this.calculateAgeDifference(personToCompare, selectedPerson, unit);
        }
        else{
            log.error("Invalid unit selected for age difference for person to compare {} with  selected person as {} " +
                    "and criteria of difference {}", personToCompare, selectedPerson, criteria);
            throw new InvalidCriteriaException("Invalid Criteria selected: "+ criteria);
        }
    }

    /***
     * This method is used to get age difference with days as unit.
     * This can further be extended to include units like months and years.
     * @param personToCompare
     * @param selectedPerson
     * @param unit
     * @return
     */
    private Long calculateAgeDifference(Person personToCompare, Person selectedPerson, String unit){
        if(unit.equalsIgnoreCase("days"))
            return DAYS.between(personToCompare.getDateOfBirth(), selectedPerson.getDateOfBirth());
        else {
            log.error("Invalid unit selected for age difference for person to compare {} with  selected person as {} " +
                    "and unit of difference {}", personToCompare, selectedPerson, unit);
            throw new InvalidCriteriaException("Invalid Unit selected: "+ unit);
        }
    }

}
