package chess.services.requests;

public class CreateGameRequest {

    private String authToken;
    private String GameName;

    public CreateGameRequest(String authToken, String GameName) {
        this.authToken = authToken;
        this.GameName = GameName;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String GameName) {
        this.GameName = GameName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
