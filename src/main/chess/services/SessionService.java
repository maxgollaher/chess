package chess.services;

import chess.services.requests.LoginRequest;
import chess.services.responses.LoginResponse;
import dataAccess.DataAccessException;

public class SessionService {

    public LoginResponse login(LoginRequest request) throws DataAccessException {
        // TODO implement here
        return null;
    }

    public void logout(String authToken) throws DataAccessException {
        // TODO implement here
    }
}
