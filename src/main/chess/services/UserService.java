package chess.services;

import chess.dataAccess.UserDao;
import chess.models.AuthToken;
import chess.services.requests.RegisterRequest;
import chess.services.responses.LoginResponse;
import dataAccess.DataAccessException;

/**
 * Interacts with the {@link UserDao} to register new users. It handles all requests
 * to the /user endpoint of the API.
 */
public class UserService {

    /**
     * The {@link UserDao} to be used to access the database
     */
    private UserDao userDao;

    /**
     * Registers a new user based on a given RegisterRequest
     * @param request a {@link RegisterRequest} to register a new user, which contains
     *                the username, password, and email
     * @return a {@link LoginResponse} containing the username and {@link AuthToken}
     * @throws DataAccessException if there is an error accessing the database or if the
     * username/email is already taken.
     */
    public LoginResponse register(RegisterRequest request) throws DataAccessException {
        // TODO implement here
        return null;
    }
}
