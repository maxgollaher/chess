package serviceTests;

import dataAccess.AuthTokenDao;
import dataAccess.DataAccessException;
import dataAccess.UserDao;
import models.AuthToken;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.SessionService;
import services.requests.LoginRequest;
import services.responses.LoginResponse;

class SessionServiceTest {

    private static final SessionService sessionService = new SessionService();
    private static final AuthTokenDao authTokenDao = AuthTokenDao.getInstance();
    private static final UserDao userDao = UserDao.getInstance();
    private static final User testUser = new User("testUser", "12345678", "test@test.com");


    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(userDao::clear);
        Assertions.assertDoesNotThrow(authTokenDao::clear);

        // check that the database is clear to start
        Assertions.assertTrue(Assertions.assertDoesNotThrow(userDao::findAll).isEmpty());
        Assertions.assertTrue(Assertions.assertDoesNotThrow(authTokenDao::findAll).isEmpty());

        // instead of calling the register function, insert directly into the DAO
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser));
    }

    @Test
    @DisplayName("Login Success")
    void login() {
        LoginRequest loginRequest = new LoginRequest(testUser.getUsername(), testUser.getPassword());
        LoginResponse loginResponse = Assertions.assertDoesNotThrow(() -> sessionService.login(loginRequest));

        // check that the login response is correct
        Assertions.assertEquals(testUser.getUsername(), loginResponse.username());
        Assertions.assertEquals(Assertions.assertDoesNotThrow(() -> authTokenDao.find(loginResponse.authToken())).getAuthToken(), loginResponse.authToken());
        Assertions.assertEquals(Assertions.assertDoesNotThrow(() -> authTokenDao.find(loginResponse.authToken())).getUsername(), testUser.getUsername());
    }

    @Test
    @DisplayName("Login Failure")
    void loginFailure() {
        LoginRequest loginRequest = new LoginRequest("invalidUsername", "invalidPassword");

        // after proving the user doesn't exist, try to log in, expecting failure
        Assertions.assertNull(Assertions.assertDoesNotThrow(() -> userDao.find(loginRequest.username())));
        Assertions.assertThrows(DataAccessException.class, () -> sessionService.login(loginRequest));
    }

    @Test
    @DisplayName("Logout Success")
    void logout() {
        LoginRequest loginRequest = new LoginRequest(testUser.getUsername(), testUser.getPassword());
        LoginResponse loginResponse = Assertions.assertDoesNotThrow(() -> sessionService.login(loginRequest));

        // check that the authToken is in the DAO
        Assertions.assertNotNull(Assertions.assertDoesNotThrow(() -> authTokenDao.find(loginResponse.authToken())));

        // log out
        Assertions.assertDoesNotThrow(() -> sessionService.logout(loginResponse.authToken()));

        // check that the authToken is not in the DAO
        Assertions.assertNull(Assertions.assertDoesNotThrow(() -> authTokenDao.find(loginResponse.authToken())));
    }

    @Test
    @DisplayName("Logout Failure")
    void logoutFailure() {
        LoginRequest loginRequest = new LoginRequest(testUser.getUsername(), testUser.getPassword());
        LoginResponse loginResponse = Assertions.assertDoesNotThrow(() -> sessionService.login(loginRequest));

        // check that the correct authToken is in the DAO
        Assertions.assertNotNull(Assertions.assertDoesNotThrow(() -> authTokenDao.find(loginResponse.authToken())));
        var correctToken = loginResponse.authToken();

        // log out with the incorrect token, expecting failure
        Assertions.assertDoesNotThrow(() -> sessionService.logout("invalidAuthToken"));

        // check that the correct AuthToken is still in the DAO
        Assertions.assertNotNull(Assertions.assertDoesNotThrow(() -> authTokenDao.find(correctToken)));
    }

    @Test
    @DisplayName("Authorize User Success")
    void authorizeUser() {
        // inject an authToken into the DAO
        AuthToken testToken = new AuthToken(testUser.getUsername());
        Assertions.assertDoesNotThrow(() -> authTokenDao.insert(testToken));

        Assertions.assertDoesNotThrow(() -> sessionService.authorizeUser(testToken.getAuthToken()));
    }

    @Test
    @DisplayName("Authorize User Failure")
    void authorizeFailure() {
        // don't inject an authToken into the DAO, try and authorize a user with an empty DAO
        Assertions.assertThrows(DataAccessException.class, () -> sessionService.authorizeUser("invalidAuthToken"));
    }
}