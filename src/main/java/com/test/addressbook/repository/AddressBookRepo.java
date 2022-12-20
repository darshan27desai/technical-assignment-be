package com.test.addressbook.repository;

import com.test.addressbook.model.Person;

import java.util.List;

public interface AddressBookRepo {
    public List<Person> getListOfPerson();

}
