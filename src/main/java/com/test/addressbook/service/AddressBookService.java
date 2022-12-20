package com.test.addressbook.service;

import com.test.addressbook.model.Person;

import java.util.List;

public interface AddressBookService {
    List<Person> getAddressBook();

    List<Person> getAddressBook(String genderFilter, String ageFilter);

}
