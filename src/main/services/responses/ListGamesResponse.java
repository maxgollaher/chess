package services.responses;

import models.AuthToken;
import models.Game;
import services.GameService;

import java.util.ArrayList;

/**
 * Response object for the {@link GameService#listGames()} method.
 */
public class ListGamesResponse {

    /**
     * The list of games that are currently in the database.
     */
    private ArrayList<Game> games;

    /**
     * Creates a new ListGamesResponse object.
     *
     * @param games the list of games that are currently in the database.
     */
    public ListGamesResponse(ArrayList<Game> games) {
        this.games = games;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }
}
