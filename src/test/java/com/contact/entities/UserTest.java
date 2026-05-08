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

    @Test
    public void testUserCreation() {
        assertNotNull(user);
    }

    @Test
    public void testUserName() {
        assertEquals("Harshita", user.getName());
    }

    @Test
    public void testUserEmail() {
        assertEquals("harshita@gmail.com", user.getEmail());
    }

    @Test
    public void testUserPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testUserRole() {
        assertEquals("ROLE_USER", user.getRole());
    }

    @Test
    public void testUserEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    public void testUserImageUrl() {
        assertEquals("default.png", user.getImageUrl());
    }

    @Test
    public void testUserAbout() {
        assertEquals("Test user", user.getAbout());
    }

    @Test
    public void testUserId() {
        assertEquals(1, user.getId());
    }

    @Test
    public void testUserContactsNotNull() {
        assertNotNull(user.getContacts());
    }

    @Test
    public void testUserContactsEmptyByDefault() {
        assertTrue(user.getContacts().isEmpty());
    }

    @Test
    public void testSetName() {
        user.setName("Sultanpure");
        assertEquals("Sultanpure", user.getName());
    }

    @Test
    public void testSetEmail() {
        user.setEmail("new@gmail.com");
        assertEquals("new@gmail.com", user.getEmail());
    }

    @Test
    public void testUserDisabled() {
        user.setEnabled(false);
        assertFalse(user.isEnabled());
    }

    @Test
    public void testToString() {
        assertNotNull(user.toString());
    }
}