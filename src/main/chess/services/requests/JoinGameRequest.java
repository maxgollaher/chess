package chess.services.requests;

public class JoinGameRequest {

    private String authToken;
    private String playerColor;
    private String gameId;

    public JoinGameRequest(String authToken, String playerColor, String gameId) {
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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
