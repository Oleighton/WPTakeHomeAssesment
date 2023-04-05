package com.example.wptakehome;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

    @Test
    public void testGetName() {
        User user = new User("Alice", "2022-04-05T10:30:00");
        assertEquals("Alice", user.getName());
    }

    @Test
    public void testGetLoginTime() {
        User user = new User("Alice", "2022-04-05T10:30:00");
        assertEquals("2022-04-05T10:30:00", user.getLoginTime());
    }

    @Test
    public void testConstructor() {
        User user = new User("Alice", "2022-04-05T10:30:00");
        assertNotNull(user);
        assertEquals("Alice", user.getName());
        assertEquals("2022-04-05T10:30:00", user.getLoginTime());
    }

}