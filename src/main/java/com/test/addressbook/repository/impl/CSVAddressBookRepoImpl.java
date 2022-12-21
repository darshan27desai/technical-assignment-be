package com.test.addressbook.repository.impl;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.test.addressbook.model.Person;
import com.test.addressbook.model.exception.PersonNotFoundException;
import com.test.addressbook.repository.AddressBookRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@Qualifier("csvAddressBookRepo")
public class CSVAddressBookRepoImpl implements AddressBookRepo {

    @Value("${address.csv.path}")
    Resource pathToCSV;
    @Override
    public List<Person> getListOfPerson(){
        List<Person> listOfPerson;
        try {
            listOfPerson =  this.readCSVLineByLine(pathToCSV);
        } catch (Exception e) {
            log.error("Unexpected error occurred. Message: {}, caused by {}",
                    e.getMessage(), e.getCause());
            throw new RuntimeException(e);
        }
        return listOfPerson;
    }

    @Override
    public Person getPersonByName(String name) {
        List<Person> listOfPerson = this.getListOfPerson();

        Person searchedPerson = listOfPerson.stream()
                .filter(person -> (person.getName() != null))
                .filter(person -> person.getName().equals(name))
                .findFirst().orElseThrow(() -> {
                    log.error("Person not found by name: {}", name);
                    return new PersonNotFoundException("Person not Found: " + name);
                });

        return searchedPerson;
    }

    private final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("dd/MM/")
            .optionalStart()
            .appendPattern("uuuu")
            .optionalEnd()
            .optionalStart()
            .appendValueReduced(ChronoField.YEAR, 2, 2, 1920)
            .optionalEnd()
            .toFormatter();

    private List<Person> readCSVLineByLine(final Resource filePath) throws Exception {

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        List<Person> listOfPerson = new ArrayList<>();

        try (Reader reader = new InputStreamReader(filePath.getInputStream())) {
            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(reader)
                    .withCSVParser(parser);

            try (CSVReader csvReader = csvReaderBuilder.build()) {
                String[] csvRecord;
                while ((csvRecord = csvReader.readNext()) != null) {
                    Person person = Person.builder()
                            .name(StringUtils.strip(csvRecord[0]))
                            .gender(Person.GenderEnum.valueOf(StringUtils.strip(csvRecord[1]).toUpperCase()))
                            .dateOfBirth(LocalDate.parse(StringUtils.strip(csvRecord[2]), formatter))
                            .build();
                    person.setName(csvRecord[0].trim());
                    listOfPerson.add(person);
                }
            }
        }
        return listOfPerson;
    }

}
