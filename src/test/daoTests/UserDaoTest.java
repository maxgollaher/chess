package daoTests;

import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class UserDaoTest {

    private static final UserDao userDao;
    private static final GameDao gameDao;

    static {
        try {
            gameDao = new GameDao();
            userDao = new UserDao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        Assertions.assertDoesNotThrow(gameDao::clear);
        Assertions.assertDoesNotThrow(userDao::clear);
    }

    @Test
    @DisplayName("Test insert User")
    void testInsert() {
        // test inserting a new User
        User testUser = new User("username", "email", "password");
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser));

        // see if the User was inserted
        var foundUser = Assertions.assertDoesNotThrow(() -> userDao.find("username"));
        Assertions.assertEquals(testUser, foundUser);
    }

    @Test
    @DisplayName("Test insert User fail")
    void testInsertFail() {
        // test insert
        User testUser = new User("username", "email", "password");
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser));

        // test inserting a duplicate User with the same primary key
        Assertions.assertThrows(DataAccessException.class, () -> userDao.insert(testUser));
    }

    @Test
    @DisplayName("Test find User")
    void testFind() {
        // test finding a User
        User testUser = new User("username", "email", "password");
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser));

        var foundUser = Assertions.assertDoesNotThrow(() -> userDao.find("username"));
        Assertions.assertEquals(testUser, foundUser);
    }

    @Test
    @DisplayName("Test find Users fail")
    void testFindFail() {
        // test that finding a nonexistent User doesn't throw an error, and that it returns null
        Assertions.assertNull(Assertions.assertDoesNotThrow(() -> userDao.find("nonexistent")));
    }

    @Test
    @DisplayName("Test find all Users")
    void testFindAll() {
        // insert several Users
        User testUser = new User("username", "email", "password");
        User testUser2 = new User("username2", "email2", "password2");
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser));
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser2));

        // see if the Users were found
        var foundUsers = Assertions.assertDoesNotThrow(userDao::findAll);
        Assertions.assertEquals(2, foundUsers.size());
        Assertions.assertTrue(foundUsers.contains(testUser));
        Assertions.assertTrue(foundUsers.contains(testUser2));
    }

    @Test
    @DisplayName("Test find all Users fail")
    void testFindAllFail() {
        // test that the result list is empty when there are no Users
        Assertions.assertTrue(Assertions.assertDoesNotThrow(userDao::findAll).isEmpty());
    }

    @Test
    @DisplayName("Test clear Users")
    void testClear() {
        // insert a User, assert that it is in the database
        User testUser = new User("username", "email", "password");
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser));
        Assertions.assertFalse(Assertions.assertDoesNotThrow(userDao::findAll).isEmpty());

        // clear the database, assert that the User is no longer in the database
        Assertions.assertDoesNotThrow(userDao::clear);
        Assertions.assertTrue(Assertions.assertDoesNotThrow(userDao::findAll).isEmpty());
    }
}
