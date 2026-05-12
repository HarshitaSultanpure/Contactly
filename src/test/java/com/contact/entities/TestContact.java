package com.contact.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestContact {

    private Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact();
    }

    @Test
    void testSetAndGetCId() {
        contact.setcId(1);
        assertEquals(1, contact.getcId());
    }

    @Test
    void testSetAndGetName() {
        contact.setName("Harshita");
        assertEquals("Harshita", contact.getName());
    }

    @Test
    void testSetAndGetSecondName() {
        contact.setSecondName("Sultanpure");
        assertEquals("Sultanpure", contact.getSecondName());
    }

    @Test
    void testSetAndGetWork() {
        contact.setWork("Software Engineer");
        assertEquals("Software Engineer", contact.getWork());
    }

    @Test
    void testSetAndGetEmail() {
        contact.setEmail("harshita@gmail.com");
        assertEquals("harshita@gmail.com", contact.getEmail());
    }

    @Test
    void testSetAndGetPhone() {
        contact.setPhone("9876543210");
        assertEquals("9876543210", contact.getPhone());
    }

    @Test
    void testSetAndGetImage() {
        contact.setImage("profile.png");
        assertEquals("profile.png", contact.getImage());
    }

    @Test
    void testSetAndGetDescription() {
        contact.setDescription("This is a test contact");
        assertEquals("This is a test contact", contact.getDescription());
    }

    @Test
    void testSetAndGetUser() {
        User user = new User();
        contact.setUser(user);

        assertNotNull(contact.getUser());
        assertEquals(user, contact.getUser());
    }

    @Test
    void testToString() {
        contact.setcId(1);
        contact.setName("Harshita");

        String result = contact.toString();

        assertNotNull(result);
    }

    @Test
    void testEquals() {
        Contact contact1 = new Contact();
        contact1.setcId(1);

        Contact contact2 = new Contact();
        contact2.setcId(1);

        assertEquals(contact1, contact2);
    }
}