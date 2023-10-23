package services.responses;

import services.GameService;
import services.requests.CreateGameRequest;

/**
 * Response object for the {@link GameService#createGame(CreateGameRequest)} method.
 */
public class CreateGameResponse {

    /**
     * The id of the game that was created.
     */
    private int gameID;

    /**
     * Creates a new CreateGameResponse object.
     *
     * @param gameId the id of the game that was created.
     */
    public CreateGameResponse(int gameId) {
        this.gameID = gameId;
    }

    public int getGameId() {
        return gameID;
    }

    public void setGameId(int gameId) {
        this.gameID = gameId;
    }
}
