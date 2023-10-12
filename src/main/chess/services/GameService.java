package chess.services;

import chess.services.requests.CreateGameRequest;
import chess.services.requests.JoinGameRequest;
import chess.services.responses.CreateGameResponse;
import chess.services.responses.ListGamesResponse;
import dataAccess.DataAccessException;

public class GameService {

    public void joinGame(JoinGameRequest request) throws DataAccessException {
        // TODO implement here
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        // TODO implement here
        return null;
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        // TODO implement here
        return null;
    }

}
