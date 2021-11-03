package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.model.User;
import com.revature.util.Factory;
import org.mockito.Mockito;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserDAO mockDAO;

    @Before
    public void setUp() throws Exception {
        mockDAO = Mockito.mock(UserDAO.class);
    }

    @After
    public void tearDown() throws Exception {
        mockDAO = null;
    }

    @Test
    public void testInsertNullUser() {
        User user = new User();
        UserService userService = Factory.getUserService();

        assertNull(userService.insertUser(user));
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setEmail("test");
        UserService userService = new UserService(mockDAO);
        when(mockDAO.insert(user)).thenReturn(user);

        assertEquals(user, userService.insertUser(user));
    }
}