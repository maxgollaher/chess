package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import services.GameService;
import services.requests.CreateGameRequest;
import services.requests.JoinGameRequest;
import services.responses.CreateGameResponse;
import services.responses.ListGamesResponse;
import spark.Response;

import java.util.Map;

/**
 * Handler for the /game endpoint
 */
public class GameHandler {

    private static final GameService gameService = new GameService();

    /**
     * Joins a game based on a given JoinGameRequest
     *
     * @param bodyObj   the body of the request, containing the player color and game id
     * @param authToken the auth token of the player, used to obtain the username of the player joining the game
     * @param response  will have an empty body
     * @throws DataAccessException when the request is invalid/missing data, and when the Service throws an exception
     *                             if the game is full
     */
    public void joinGame(Map<String, Object> bodyObj, String authToken, Response response) throws DataAccessException {
        // add the auth token to the body and create a JoinGameRequest
        bodyObj.put("authToken", authToken);
        JoinGameRequest joinGameRequest = new Gson().fromJson(new Gson().toJson(bodyObj), JoinGameRequest.class);
        if (joinGameRequest.gameID() == 0) {
            response.status(400);
            throw new DataAccessException("bad request");
        }
        try {
            gameService.joinGame(joinGameRequest);
        } catch (DataAccessException e) {
            response.status(403);
            throw e;
        }

        response.body(new Gson().toJson(Map.of("message", "Success")));
    }

    /**
     * Creates a new game based on a given CreateGameRequest
     *
     * @param bodyObj  the body of the request, containing the game name
     * @param response will be modified to contain the game id of the created game
     * @throws DataAccessException when the request is invalid/missing data
     */
    public void createGame(Map<String, Object> bodyObj, Response response) throws DataAccessException {
        CreateGameRequest createGameRequest = new Gson().fromJson(new Gson().toJson(bodyObj), CreateGameRequest.class);
        if (createGameRequest.gameName() == null) {
            response.status(400);
            throw new DataAccessException("bad request");
        }
        CreateGameResponse createGameResponse = gameService.createGame(createGameRequest);
        response.body(new Gson().toJson(createGameResponse));
    }

    /**
     * Lists all games that are currently in progress.
     *
     * @param response will be modified to contain a list of all games that are currently in progress.
     * @throws DataAccessException if there is an error accessing the database.
     */
    public void listGames(Response response) throws DataAccessException {
        ListGamesResponse listGamesResponse = gameService.listGames();
        response.body(new Gson().toJson(listGamesResponse));
    }
}
