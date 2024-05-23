package com.example.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.service.UserService;

@SpringBootTest
class UserServiceTests {
    @Autowired
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void createData() {
        removeData();

        user = userService.create(new UserEntity("mail", "pass"));
        userService.create(new UserEntity("mailru", "pass"));
        userService.create(new UserEntity("mailcom", "pass"));
    }

    @AfterEach
    void removeData() {
        userService.getAll().forEach(item -> userService.delete(item.getId()));
    }

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> userService.get(0L));
    }

    @Test
    void createTest() {
        Assertions.assertEquals(3, userService.getAll().size());
        Assertions.assertEquals(user, userService.get(user.getId()));
    }

    @Test
    void createNotUniqueTest() {
        final UserEntity nonUniqueUser = new UserEntity("mailru", "pass");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.create(nonUniqueUser));
    }

    @Test
    void createNullableTest() {
        final UserEntity nullableUser = new UserEntity(null, null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userService.create(nullableUser));
    }

    @Test
    void updateTest() {
        final String testString = "TEST";
        final String lastName = user.getLogin();
        final UserEntity updatedUser = userService.update(user.getId(),
                new UserEntity(testString, testString));
        Assertions.assertEquals(3, userService.getAll().size());
        Assertions.assertEquals(updatedUser, userService.get(user.getId()));
        Assertions.assertNotEquals(lastName, updatedUser.getLogin());
    }
}
