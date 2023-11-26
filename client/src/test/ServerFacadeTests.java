import chess.ChessGame;
import exception.ResponseException;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.ServerFacade;

public class ServerFacadeTests {

    private static final String serverURL = "http://localhost:8080";
    public static final ServerFacade serverFacade = new ServerFacade(serverURL);

    @BeforeEach
    public void setup() {
        // Clear the DB before each test
        Assertions.assertDoesNotThrow(serverFacade::clear);
    }


    @Test
    @DisplayName("Register Success")
    public void register() {
        String[] params = {"username", "password", "email"};
        Assertions.assertDoesNotThrow(() -> serverFacade.register(params));
    }

    @Test
    @DisplayName("Register Failure")
    public void registerFailure() {
        String[] params = {"username", "password", "email"};
        Assertions.assertDoesNotThrow(() -> serverFacade.register(params));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register(params));
    }

    @Test
    @DisplayName("Login Success")
    public void login() {
        String[] registerParams = {"username", "password", "email"};
        String[] loginParams = {"username", "password"};
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register(registerParams));
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(response.authToken()));
        Assertions.assertDoesNotThrow(() -> serverFacade.login(loginParams));
    }

    @Test
    @DisplayName("Login Failure")
    public void loginFailure() {
        String[] loginParams = {"invalid", "invalid"};
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login(loginParams));
    }

    @Test
    @DisplayName("Logout Success")
    public void logout() {
        String[] registerParams = {"username", "password", "email"};
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register(registerParams));
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(response.authToken()));
    }

    @Test
    @DisplayName("Logout Failure")
    public void logoutFailure() {
        // logout with an invalid auth token
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout("invalidAuthToken"));
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGame() {
        String[] registerParams = {"username", "password", "email"};
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register(registerParams));
        String[] createGameParams = {"gameName"};
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(createGameParams, response.authToken()));
    }

    @Test
    @DisplayName("Create Game Failure")
    public void createGameFailure() {
        String[] createGameParams = {"gameName"};
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.createGame(createGameParams, "invalidAuthToken"));
    }


    @Test
    @DisplayName("List Games Success")
    public void listGames() {
        // add a few games
        String[] registerParams = {"username", "password", "email"};
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register(registerParams));
        String[][] createGameParams = {{"gameName1"}, {"gameName2"}, {"gameName3"}};
        for (String[] params : createGameParams) {
            Assertions.assertDoesNotThrow(() -> serverFacade.createGame(params, response.authToken()));
        }
        var games = Assertions.assertDoesNotThrow(() -> serverFacade.listGames(response.authToken())).games();

        // check that the games are in the response
        Assertions.assertEquals(3, games.size());
        for (Game game : games) {
            Assertions.assertTrue(game.getGameName().startsWith("gameName"));
        }
    }

    @Test
    @DisplayName("List Games Failure")
    public void listGamesFailure() {
        // list games with an invalid auth token
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames("invalidAuthToken"));
    }

    @Test
    @DisplayName("Join Game Success")
    public void joinGame() {
        // create a game
        String[] registerParams = {"username", "password", "email"};
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register(registerParams));
        String[] createGameParams = {"gameName"};
        var createGameResponse = Assertions.assertDoesNotThrow(() -> serverFacade.createGame(createGameParams, response.authToken()));
        var gameID = createGameResponse.gameID();

        // join the game
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(gameID, ChessGame.TeamColor.WHITE, response.authToken()));
    }

    @Test
    @DisplayName("Join Game Failure")
    public void joinGameFailure() {
        // join a game that doesn't exist
        String[] registerParams = {"username", "password", "email"};
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register(registerParams));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(1, ChessGame.TeamColor.WHITE, response.authToken()));
    }

}
