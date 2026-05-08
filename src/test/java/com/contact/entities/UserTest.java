package com.contact.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Harshita");
        user.setEmail("harshita@gmail.com");
        user.setPassword("password123");
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setImageUrl("default.png");
        user.setAbout("Test user");
    }

    // Test 1 - User object creation
    @Test
    public void testUserCreation() {
        assertNotNull(user);
    }

    // Test 2 - User name
    @Test
    public void testUserName() {
        assertEquals("Harshita", user.getName());
    }

    // Test 3 - User email
    @Test
    public void testUserEmail() {
        assertEquals("harshita@gmail.com", user.getEmail());
    }

    // Test 4 - User password
    @Test
    public void testUserPassword() {
        assertEquals("password123", user.getPassword());
    }

    // Test 5 - User role
    @Test
    public void testUserRole() {
        assertEquals("ROLE_USER", user.getRole());
    }

    // Test 6 - User enabled
    @Test
    public void testUserEnabled() {
        assertTrue(user.isEnabled());
    }

    // Test 7 - User imageUrl
    @Test
    public void testUserImageUrl() {
        assertEquals("default.png", user.getImageUrl());
    }

    // Test 8 - User about
    @Test
    public void testUserAbout() {
        assertEquals("Test user", user.getAbout());
    }

    // Test 9 - User id
    @Test
    public void testUserId() {
        assertEquals(1, user.getId());
    }

    // Test 10 - User contacts list not null
    @Test
    public void testUserContactsNotNull() {
        assertNotNull(user.getContacts());
    }

    // Test 11 - User contacts list empty by default
    @Test
    public void testUserContactsEmptyByDefault() {
        assertTrue(user.getContacts().isEmpty());
    }

    // Test 12 - Set and get name
    @Test
    public void testSetName() {
        user.setName("Sultanpure");
        assertEquals("Sultanpure", user.getName());
    }

    // Test 13 - Set and get email
    @Test
    public void testSetEmail() {
        user.setEmail("new@gmail.com");
        assertEquals("new@gmail.com", user.getEmail());
    }

    // Test 14 - User disabled
    @Test
    public void testUserDisabled() {
        user.setEnabled(false);
        assertFalse(user.isEnabled());
    }

    // Test 15 - toString not null
    @Test
    public void testToString() {
        assertNotNull(user.toString());
    }
}