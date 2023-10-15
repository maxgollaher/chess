package chess.services;

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
     * The {@link GameDao} to be used to access the database
     */
    private GameDao gameDao;

    public void joinGame(JoinGameRequest request) throws DataAccessException {
        // TODO implement here
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        // TODO implement here
        return null;
    }

    public ListGamesResponse listGames(AuthToken authToken) throws DataAccessException {
        // TODO implement here
        return null;
    }

}
