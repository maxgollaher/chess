package serviceTests;

import dataAccess.AuthTokenDao;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.UserService;
import requests.RegisterRequest;
import responses.LoginResponse;

class UserServiceTest {

    private static final UserDao userDao;
    private static final AuthTokenDao authTokenDao;
    private static final GameDao gameDao;
    private static final UserService userService = new UserService();
    private static final User testUser = new User("testUser", "12345678", "test@test.com");

    static {
        try {
            authTokenDao = new AuthTokenDao();
            userDao = new UserDao();
            gameDao = new GameDao();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(gameDao::clear);
        Assertions.assertDoesNotThrow(userDao::clear);
        Assertions.assertDoesNotThrow(authTokenDao::clear);

        // check that the database is clear to start
        Assertions.assertTrue(Assertions.assertDoesNotThrow(userDao::findAll).isEmpty());
        Assertions.assertTrue(Assertions.assertDoesNotThrow(authTokenDao::findAll).isEmpty());
    }

    @Test
    @DisplayName("Register Success")
    public void register() {

        // register the user
        RegisterRequest registerRequest = new RegisterRequest(testUser.getUsername(), testUser.getPassword(), testUser.getEmail());
        LoginResponse loginResponse = Assertions.assertDoesNotThrow(() -> userService.register(registerRequest));

        // check that the loginResponse is correct and the User/authToken are in the DAO
        Assertions.assertEquals(testUser.getUsername(), loginResponse.username());
        Assertions.assertEquals(testUser.getUsername(), Assertions.assertDoesNotThrow(() -> authTokenDao.find(loginResponse.authToken())).getUsername());
        Assertions.assertEquals(testUser, Assertions.assertDoesNotThrow(() -> userDao.find(testUser.getUsername())));
    }

    @Test
    @DisplayName("Register Fail")
    public void registerFail() {

        // attempt to register using the same information twice
        RegisterRequest registerRequest = new RegisterRequest(testUser.getUsername(), testUser.getPassword(), testUser.getEmail());
        Assertions.assertDoesNotThrow(() -> userService.register(registerRequest));
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(registerRequest));
    }


}