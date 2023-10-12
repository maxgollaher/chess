package chess.services.requests;

public class CreateGameRequest {
    private String GameName;

    public CreateGameRequest(String GameName) {
        this.GameName = GameName;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String GameName) {
        this.GameName = GameName;
    }
}
