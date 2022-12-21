package com.test.addressbook.controller;

import com.test.addressbook.model.DifferenceInAge;
import com.test.addressbook.model.Person;
import com.test.addressbook.service.AddressBookService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
public class AddressBookController {

    private AddressBookService addressBookService;

    public AddressBookController(@Autowired
                                 @Qualifier("csvAddressBookService")
                                 final AddressBookService csvAddressBookService){
        this.addressBookService = csvAddressBookService;
    }

    @ResponseBody
    @GetMapping(path = "/persons", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Person>> findPerson(
            @RequestHeader(value = "X-Request-Correlation-Id") String requestCorrelationId,
            @ApiParam(value = "Gender values that need to be considered for filter", allowableValues = "Male, Female, All", defaultValue = "All")
            @Valid @RequestParam(value = "genderFilterCriteria", required = false, defaultValue="All") String genderCriteria,

            @ApiParam(value = "Filter criteria for the Age i.e Youngest or Oldest", allowableValues = "Youngest, Oldest, All", defaultValue = "All")
            @Valid @RequestParam(value = "ageFilterCriteria", required = false, defaultValue="All") String ageCriteria) {

        List<Person> resultantList = addressBookService.getAddressBook(genderCriteria, ageCriteria);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Total-Count", String.valueOf(resultantList.size()));

        return new ResponseEntity<>(resultantList, httpHeaders, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(path = "/persons/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DifferenceInAge> differenceInAge(
            @Valid @PathVariable(value= "name", required = true) String name,
            @RequestHeader(value = "X-Request-Correlation-Id") String requestCorrelationId,
            @ApiParam(value = "Comparison Unit", allowableValues = "Days", defaultValue = "Days")
            @Valid @RequestParam(value = "comparisonUnit", defaultValue="Days") String comparisonUnit,
            @ApiParam(value = "Age Comparison criteria possible values Older", allowableValues = "Older", defaultValue = "Older")
            @Valid @RequestParam(value = "ageComparison", required = false, defaultValue="Older") String ageComparison,
            @ApiParam(value = "Person selected from address book to compare")
            @Valid @RequestParam(value = "personToCompare", required = true) String personToCompare) throws UnsupportedEncodingException {

        DifferenceInAge differenceInAge =  addressBookService.getDifferenceOfAge(URLDecoder.decode(name, "UTF-8"), URLDecoder.decode(personToCompare, "UTF-8"), comparisonUnit, ageComparison);

        return new ResponseEntity<>(differenceInAge, HttpStatus.OK);

    }

}
