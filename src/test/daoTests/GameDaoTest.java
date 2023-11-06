package daoTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameDaoTest {

    private static final GameDao gameDao;
    private static final UserDao userDao;

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
    @DisplayName("Test insert Game")
    void testInsert() {
        // test inserting a new Game
        models.Game testGame = new models.Game("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));

        // see if the Game was inserted
        var foundGame = Assertions.assertDoesNotThrow(() -> gameDao.find(testGame.getGameID()));
        Assertions.assertEquals(testGame, foundGame);
    }

    @Test
    @DisplayName("Test insert Game fail")
    void testInsertFail() {
        // test insert
        models.Game testGame = new models.Game("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));

        // test inserting a duplicate Game with the same primary key
        Assertions.assertThrows(DataAccessException.class, () -> gameDao.insert(testGame));
    }

    @Test
    @DisplayName("Test claim spot")
    void testClaimSpot() {
        // make a test game and insert it
        models.Game testGame = new models.Game("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));

        // add a user to the userDao to satisfy foreign key constraints, claim spot
        User testUser = new User("testUser", "email", "password");
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser));
        Assertions.assertDoesNotThrow(() -> gameDao.claimSpot(testUser.getUsername(), ChessGame.TeamColor.WHITE, testGame.getGameID()));

        // assert that the spot has been claimed
        var foundUsername = Assertions.assertDoesNotThrow(() -> gameDao.find(testGame.getGameID()).getWhiteUsername());
        Assertions.assertEquals(foundUsername, testUser.getUsername());
    }

    @Test
    @DisplayName("Test claim spot fail")
    void testClaimSpotFail() {
        // make a test game and insert it
        models.Game testGame = new models.Game("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));

        // add a user to the userDao to satisfy foreign key constraints, claim spot
        User testUser = new User("testUser", "email", "password");
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser));
        Assertions.assertDoesNotThrow(() -> gameDao.claimSpot(testUser.getUsername(), ChessGame.TeamColor.WHITE, testGame.getGameID()));

        // try to claim the same spot again, assert that it throws an error
        Assertions.assertThrows(DataAccessException.class, () -> gameDao.claimSpot(testUser.getUsername(), ChessGame.TeamColor.WHITE, testGame.getGameID()));
    }

    @Test
    @DisplayName("Test find Game")
    void testFind() {
        // test finding a Game
        models.Game testGame = new models.Game("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));

        var foundGame = Assertions.assertDoesNotThrow(() -> gameDao.find(testGame.getGameID()));
        Assertions.assertEquals(testGame, foundGame);
    }

    @Test
    @DisplayName("Test find Game fail")
    void testFindFail() {
        // test that finding a nonexistent Game doesn't throw an error, and that it returns null
        Assertions.assertNull(Assertions.assertDoesNotThrow(() -> gameDao.find(0)));
    }

    @Test
    @DisplayName("Test find all Games")
    void testFindAll() {
        // insert several tokens
        models.Game testGame = new models.Game("testGame");
        models.Game testGame2 = new models.Game("testGame2");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame2));

        // see if the Games were found
        var foundGames = Assertions.assertDoesNotThrow(gameDao::findAll);
        Assertions.assertEquals(2, foundGames.size());
        Assertions.assertTrue(foundGames.contains(testGame));
        Assertions.assertTrue(foundGames.contains(testGame2));
    }

    @Test
    @DisplayName("Test find all Games fail")
    void testFindAllFail() {
        // test that the result list is empty when there are no Games
        Assertions.assertTrue(Assertions.assertDoesNotThrow(gameDao::findAll).isEmpty());
    }

    @Test
    @DisplayName("Test clear Games")
    void testClear() {
        // insert a token, assert that it is in the database
        models.Game testGame = new models.Game("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));
        Assertions.assertFalse(Assertions.assertDoesNotThrow(gameDao::findAll).isEmpty());

        // clear the database, assert that the token is no longer in the database
        Assertions.assertDoesNotThrow(gameDao::clear);
        Assertions.assertTrue(Assertions.assertDoesNotThrow(gameDao::findAll).isEmpty());
    }
}
