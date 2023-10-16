package chess.services.requests;

import chess.models.AuthToken;
import chess.services.GameService;


/**
 * Request object for the {@link GameService#joinGame(JoinGameRequest)} method.
 */
public class JoinGameRequest {

    /**
     * The {@link AuthToken} of the user that is joining the game.
     */
    private AuthToken authToken;

    /**
     * The color of the player that is joining the game. Either "white" or "black".
     */
    private String playerColor;

    /**
     * The id of the game that the player is joining.
     */
    private String gameId;

    /**
     * Creates a new JoinGameRequest object.
     *
     * @param authToken   the {@link AuthToken} of the user that is joining the game.
     * @param playerColor the color of the player that is joining the game. Either "white" or "black".
     * @param gameId      the id of the game that the player is joining.
     */
    public JoinGameRequest(AuthToken authToken, String playerColor, String gameId) {
        this.authToken = authToken;
        this.playerColor = playerColor;
        this.gameId = gameId;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
