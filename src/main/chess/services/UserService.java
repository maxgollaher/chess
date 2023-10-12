package chess.services;

import chess.dataAccess.UserDao;
import chess.services.requests.RegisterRequest;
import chess.services.responses.LoginResponse;
import dataAccess.DataAccessException;

public class UserService {

    private UserDao userDao;

    public LoginResponse register(RegisterRequest request) throws DataAccessException {
        // TODO implement here
        return null;
    }
}
