package chess.services;

import chess.dataAccess.AuthTokenDao;
import chess.dataAccess.GameDao;
import chess.models.AuthToken;
import chess.services.requests.CreateGameRequest;
import chess.services.requests.JoinGameRequest;
import chess.services.responses.CreateGameResponse;
import chess.services.responses.ListGamesResponse;
import dataAccess.DataAccessException;

/**
 * The Game Service class handles all requests to the /game endpoint of the API.
 * It interacts with the {@link GameDao} to create games, join games, and list games.
 */
public class GameService {

    /**
     * The {@link GameDao} to be used to access the game database
     */
    private GameDao gameDao;

    /**
     * The {@link AuthTokenDao} to be used to access the {@link AuthToken} database
     */
    private AuthTokenDao authTokenDao;


    /**
     * Joins a player to a game based on a given JoinGameRequest
     *
     * @param request a {@link JoinGameRequest} to join a game, which contains the player's
     *                {@link AuthToken}, player color, and the game id.
     * @throws DataAccessException if there is an error accessing the database or if the game
     *                             position is already taken.
     */
    public void joinGame(JoinGameRequest request) throws DataAccessException {
        // TODO implement here
    }

    /**
     * Creates a new game based on a given CreateGameRequest
     *
     * @param request a {@link CreateGameRequest} to create a new game, which contains the
     *                player's {@link AuthToken} and the game name.
     * @return a {@link CreateGameResponse} containing the game id of the created game.
     * @throws DataAccessException if there is an error accessing the database or the user lacks authorization.
     */
    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        // TODO implement here
        return null;
    }

    /**
     * Lists all games that are currently in progress.
     *
     * @param authToken the {@link AuthToken} of the user that is listing the games.
     * @return a {@link ListGamesResponse} containing a list of all games that are currently in progress.
     * @throws DataAccessException if there is an error accessing the database or the user lacks authorization.
     */

    public ListGamesResponse listGames(AuthToken authToken) throws DataAccessException {
        // TODO implement here
        return null;
    }

}
