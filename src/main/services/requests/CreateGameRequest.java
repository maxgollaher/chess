package services.requests;

import models.AuthToken;
import services.GameService;

/**
 * Request object for the {@link GameService#createGame(CreateGameRequest)} method.
 */
public class CreateGameRequest {

    /**
     * The {@link AuthToken} of the user that is creating the game.
     */
    private AuthToken authToken;

    /**
     * The name of the game that is being created.
     */
    private String gameName;

    /**
     * Creates a new CreateGameRequest object.
     *
     * @param authToken the {@link AuthToken} of the user that is creating the game.
     * @param gameName  the name of the game that is being created.
     */
    public CreateGameRequest(AuthToken authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
