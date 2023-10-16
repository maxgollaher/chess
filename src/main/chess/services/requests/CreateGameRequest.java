package chess.services.requests;

import chess.models.AuthToken;
import chess.services.GameService;

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
    private String GameName;

    /**
     * Creates a new CreateGameRequest object.
     *
     * @param authToken the {@link AuthToken} of the user that is creating the game.
     * @param GameName the name of the game that is being created.
     */
    public CreateGameRequest(AuthToken authToken, String GameName) {
        this.authToken = authToken;
        this.GameName = GameName;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String GameName) {
        this.GameName = GameName;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
