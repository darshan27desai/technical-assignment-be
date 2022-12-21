package com.test.addressbook.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AddressBookIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testGetNumberOfMales(){

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Request-Correlation-ID", UUID.randomUUID().toString());

        HttpEntity<String> request =
                new HttpEntity<String>(headers);

        ResponseEntity<String> response = this.testRestTemplate.exchange("/persons?genderFilterCriteria=Male", HttpMethod.GET, request, String.class);
        assertEquals("1", response.getHeaders().get("X-Total-Count").get(0));
    }
    @Test
    public void testGetOldestPerson() throws JSONException {

        String expected = "[{\"name\":\"Jane Doe\",\"gender\":\"Female\",\"dateOfBirth\":\"1970-11-01\"}]";

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Request-Correlation-ID", UUID.randomUUID().toString());

        HttpEntity<String> request =
                new HttpEntity<String>(headers);

        ResponseEntity<String> response = this.testRestTemplate.exchange("/persons?ageFilterCriteria=Oldest", HttpMethod.GET, request, String.class);
        JSONAssert.assertEquals(expected, response.getBody().toString(), false);
    }

    @Test
    public void testGetDifferenceOfAge() throws JSONException {


        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Request-Correlation-ID", UUID.randomUUID().toString());

        HttpEntity<String> request =
                new HttpEntity<String>(headers);

        ResponseEntity<String> response = this.testRestTemplate.exchange(
                "/persons/Jane%20Doe?ageComparison=Older&comparisonUnit=Days&personToCompare=John%20Doe",
                HttpMethod.GET, request, String.class);
        log.info(response.getBody().toString());
    }
}
