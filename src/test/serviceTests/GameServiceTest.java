package serviceTests;

import chess.ChessGame;
import dataAccess.AuthTokenDao;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;
import models.AuthToken;
import models.Game;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.GameService;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;

class GameServiceTest {

    private static final GameService gameService = new GameService();
    private static final GameDao gameDao;
    private static final AuthTokenDao authTokenDao;
    private static final UserDao userDao;
    private static final User testUser = new User("testUser", "12345678", "test@test.com");
    private static final AuthToken testToken = new AuthToken(testUser.getUsername());

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

        // register a user and add an authToken to the DAO
        Assertions.assertDoesNotThrow(() -> userDao.insert(testUser));
        Assertions.assertDoesNotThrow(() -> authTokenDao.insert(testToken));
    }

    @Test
    @DisplayName("Join Game Success")
    void joinGame() {
        // insert a game into the DAO rather than calling the createGame function
        Game testGame = new Game("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));

        // join the game
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, testGame.getGameID(), testToken.getAuthToken());
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(joinGameRequest));

        // check to see if the player is the correct color and the BLACK color is still available
        Assertions.assertEquals(testUser.getUsername(), Assertions.assertDoesNotThrow(() -> gameDao.find(testGame.getGameID())).getWhiteUsername());
        Assertions.assertNull(Assertions.assertDoesNotThrow(() -> gameDao.find(testGame.getGameID())).getBlackUsername());
    }

    @Test
    @DisplayName("Join Game Failure")
    void joinFailure() {
        // insert a game into the DAO rather than calling the createGame function
        Game testGame = new Game("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));

        // join the game, with the wrong gameID, expecting failure
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, 0, testToken.getAuthToken()); // the gameID will never be 0
        Assertions.assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest));
    }

    @Test
    @DisplayName("Create Game Success")
    void createGame() {
        // create a game, expecting success
        CreateGameRequest createGameRequest = new CreateGameRequest("testGame");
        CreateGameResponse createGameResponse = Assertions.assertDoesNotThrow(() -> gameService.createGame(createGameRequest));

        // check that the game is in the DAO
        Game testGame = Assertions.assertDoesNotThrow(() -> gameDao.find(createGameResponse.gameID()));
        Assertions.assertNotNull(testGame);
        Assertions.assertEquals(createGameRequest.gameName(), testGame.getGameName());
    }

    @Test
    @DisplayName("Create Game Failure")
    void createFailure() {
        // insert a game into the DAO, modify its id to the next id, and attempt to create a game with that same id
        Game testGame = new Game("testGame");
        testGame.setGameID(testGame.getGameID() + 1);
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame));

        // create a game with the same id, expecting failure
        CreateGameRequest createGameRequest = new CreateGameRequest("testGame");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest));
    }


    @Test
    @DisplayName("List Games Success")
    void listGames() {
        // create two games and add them to the DAO
        Game testGame1 = new Game("testGame1");
        Game testGame2 = new Game("testGame2");
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame1));
        Assertions.assertDoesNotThrow(() -> gameDao.insert(testGame2));

        // list the games and verify the response object
        ListGamesResponse listGamesResponse = Assertions.assertDoesNotThrow(gameService::listGames);
        Assertions.assertEquals(2, listGamesResponse.games().size());
        Assertions.assertTrue(listGamesResponse.games().contains(testGame1));
        Assertions.assertTrue(listGamesResponse.games().contains(testGame2));
    }
}