package chess.services.responses;

import chess.models.AuthToken;
import chess.models.Game;
import chess.services.GameService;

import java.util.ArrayList;

/**
 * Response object for the {@link GameService#listGames(AuthToken)} method.
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
