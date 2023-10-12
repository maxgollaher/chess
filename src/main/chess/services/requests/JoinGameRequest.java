package chess.services.requests;

public class JoinGameRequest {
    private String playerColor;
    private String gameId;

    public JoinGameRequest(String playerColor, String gameId) {
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
}
