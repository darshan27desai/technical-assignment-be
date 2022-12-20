package com.test.addressbook.controller;

import com.test.addressbook.model.Person;
import com.test.addressbook.service.impl.CSVAddressBookServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@WebMvcTest(AddressBookController.class)
public class AddressBookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CSVAddressBookServiceImpl csvAddressBookService;

    private HttpHeaders httpHeaders = new HttpHeaders();

    @BeforeEach
    public void init(){
        httpHeaders.add("X-Request-Correlation-Id", UUID.randomUUID().toString());
    }

    @Test
    public void testGetPersonDetailsInvalidFilter() throws Exception {

        String expected = "{\"code\":\"VALIDATION_ERROR\",\"message\":\"ERROR - Instance value (\\\"Invalid\\\") not found in enum (possible values: [\\\"Male\\\",\\\"Female\\\",\\\"All\\\"]): []\",\"cause\":\"Bad Request\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons")
                .headers(httpHeaders).queryParam("genderFilterCriteria", "Invalid");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

    }
    @Test
    public void testGetPersonDetailsListSizePositive() throws Exception {

        List<Person> personList  = new ArrayList<>();

        Person person1 = Person.builder().name("John Doe")
                .dateOfBirth(LocalDate.now()).gender(Person.GenderEnum.MALE).build();

        Person person2 = Person.builder().name("Jane Doe")
                .dateOfBirth(LocalDate.now()).gender(Person.GenderEnum.FEMALE).build();

        personList.add(person1);
        personList.add(person2);


        when(csvAddressBookService.getAddressBook(anyString(), anyString())).thenReturn(personList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons")
                                                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().stringValues("X-Total-Count", "2"));
    }

    @Test
    public void testGetPersonDetailsListSizeZero() throws Exception {
        List<Person> personList  = new ArrayList<>();
        when(csvAddressBookService.getAddressBook(anyString(), anyString())).thenReturn(personList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons")
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().stringValues("X-Total-Count", "0"));
    }

    @Test
    public void testDifferenceOfAgeInvalidFilter() throws Exception{

        String expected = "{\"code\":\"VALIDATION_ERROR\",\"message\":\"ERROR - Instance value (\\\"None\\\") not found in enum (possible values: [\\\"Days\\\"]): []\",\"cause\":\"Bad Request\"}";


        RequestBuilder request = MockMvcRequestBuilders.get("/persons/john")
                .headers(httpHeaders).queryParam("comparisonUnit", "None")
                .queryParam("ageComparison", "Older")
                .queryParam("personToCompare", "jane");

        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

}
