package services.requests;

import services.GameService;

/**
 * Request object for the {@link GameService#createGame(CreateGameRequest)} method.
 */
public class CreateGameRequest {

    /**
     * The name of the game that is being created.
     */
    private String gameName;

    /**
     * Creates a new CreateGameRequest object.
     *
     * @param gameName  the name of the game that is being created.
     */
    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

}
