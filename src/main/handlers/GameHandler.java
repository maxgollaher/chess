package handlers;

import java.util.Map;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import services.GameService;
import services.requests.CreateGameRequest;
import services.responses.CreateGameResponse;
import spark.Response;

public class GameHandler {

    private static final GameService gameService = new GameService();


    public void joinGame() {
        // TODO implement here
    }

    public void createGame(Map bodyObj, Response response) throws DataAccessException {
        CreateGameRequest createGameRequest = new Gson().fromJson(bodyObj.toString(), CreateGameRequest.class);
        if (createGameRequest.getGameName() == null) {
            response.status(400);
            throw new DataAccessException("bad request");
        }
        CreateGameResponse createGameResponse = gameService.createGame(createGameRequest);
        response.body(new Gson().toJson(createGameResponse));
    }

    public void startGame() {
        // TODO implement here
    }
}
