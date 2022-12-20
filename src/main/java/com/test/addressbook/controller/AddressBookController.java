package com.test.addressbook.controller;

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

}
