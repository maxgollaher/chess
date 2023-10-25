package serviceTests;

import dataAccess.AuthTokenDao;
import dataAccess.GameDao;
import dataAccess.UserDao;
import models.AuthToken;
import models.Game;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.AdminService;

class AdminServiceTest {

    private static final UserDao userDao = UserDao.getInstance();
    private static final AuthTokenDao authTokenDao = AuthTokenDao.getInstance();
    private static final GameDao gameDao = GameDao.getInstance();
    private static final AdminService testAdmin = new AdminService();

    @BeforeEach
    void setup() {
        Assertions.assertDoesNotThrow(userDao::clear);
        Assertions.assertDoesNotThrow(authTokenDao::clear);
        Assertions.assertDoesNotThrow(gameDao::clear);

        // check that the database is clear to start
        Assertions.assertTrue(Assertions.assertDoesNotThrow(userDao::findAll).isEmpty());
        Assertions.assertTrue(Assertions.assertDoesNotThrow(authTokenDao::findAll).isEmpty());
        Assertions.assertTrue(Assertions.assertDoesNotThrow(gameDao::findAll).isEmpty());
    }
    @Test
    @DisplayName("Clear Test")
    public void clearDatabase() {

        // add some data to the database
        User testUser = new User("testUser", "password", "test@test.com");
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser));
        AuthToken testToken = new AuthToken("testUser");
        Assertions.assertDoesNotThrow(() -> authTokenDao.insert(testToken));
        Game testGame = new Game("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));

        // check the database is not empty
        Assertions.assertFalse(Assertions.assertDoesNotThrow(userDao::findAll).isEmpty());
        Assertions.assertFalse(Assertions.assertDoesNotThrow(authTokenDao::findAll).isEmpty());
        Assertions.assertFalse(Assertions.assertDoesNotThrow(gameDao::findAll).isEmpty());

        Assertions.assertDoesNotThrow(testAdmin::clearDatabase);

        // check that the database is clear
        Assertions.assertTrue(Assertions.assertDoesNotThrow(userDao::findAll).isEmpty());
        Assertions.assertTrue(Assertions.assertDoesNotThrow(authTokenDao::findAll).isEmpty());
        Assertions.assertTrue(Assertions.assertDoesNotThrow(gameDao::findAll).isEmpty());
    }


}