package com.test.addressbook.repository;

import com.test.addressbook.model.Person;

import java.util.List;

public interface AddressBookRepo {
    List<Person> getListOfPerson();

    Person getPersonByName(String name);

}
