package services.requests;

import chess.ChessGame;
import services.GameService;


/**
 * Request object for the {@link GameService#joinGame(JoinGameRequest)} method.
 */
public class JoinGameRequest {

    private String authToken;

    /**
     * The color of the player that is joining the game. Either "white" or "black".
     */
    private ChessGame.TeamColor playerColor;

    /**
     * The id of the game that the player is joining.
     */
    private int gameID;

    /**
     * Creates a new JoinGameRequest object.
     *
     * @param playerColor the color of the player that is joining the game. Either "white" or "black".
     * @param gameId      the id of the game that the player is joining.
     */
    public JoinGameRequest(ChessGame.TeamColor playerColor, int gameId) {
        this.playerColor = playerColor;
        this.gameID = gameId;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
