package com.example.wptakehome;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class UserStoredInfoTest {

    private UserStoredInfo userStoredInfo;
    private HashMap<String, String> testData;

    @Before
    public void setUp() {
        userStoredInfo = UserStoredInfo.getInstance();
        testData = new HashMap<>();
        testData.put("user1", "2022-01-01 12:00:00");
        testData.put("user2", "2022-01-02 12:00:00");
    }

    @Test
    public void testGetInstance() {
        assertNotNull(userStoredInfo);
    }

    @Test
    public void testGetData() {
        userStoredInfo.setData(testData);
        assertEquals(testData, userStoredInfo.getData());
    }

    @Test
    public void testAddUser() {
        User user = new User("user3", "2022-01-03 12:00:00");
        userStoredInfo.addUser(user);
        assertTrue(userStoredInfo.containsUser("user3"));
    }

    @Test
    public void testContainsUser() {
        userStoredInfo.setData(testData);
        assertTrue(userStoredInfo.containsUser("user1"));
        assertFalse(userStoredInfo.containsUser("user3"));
    }

    @Test
    public void testSetData() {
        userStoredInfo.setData(testData);
        assertEquals(testData, userStoredInfo.getData());
    }

}