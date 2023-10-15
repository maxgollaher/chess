package chess.services;

import chess.dataAccess.AuthTokenDao;
import chess.models.AuthToken;
import chess.services.requests.LoginRequest;
import chess.services.responses.LoginResponse;
import dataAccess.DataAccessException;

/**
 * This class is responsible for handling all requests to the session
 * endpoint of the API. It interacts with the {@link AuthTokenDao} to login and logout
 * users.
 */
public class SessionService {

    /**
     * The {@link AuthTokenDao} to be used to access the database
     */
    private AuthTokenDao authTokenDao;

    /**
     * Logs in a user based on a given {@link LoginRequest}
     * @param request a {@link LoginRequest} to log in a user, which contains
     *                the username and password.
     * @return a {@link LoginResponse} object containing the username and {@link AuthToken}
     * @throws DataAccessException if there is an error accessing the database or if the
     * username/password is incorrect
     */
    public LoginResponse login(LoginRequest request) throws DataAccessException {
        // TODO implement here
        return null;
    }

    /**
     * Logs out a user based on a given token
     * @param authToken the {@link AuthToken} of the user to logout
     * @throws DataAccessException if there is an error accessing the database
     * or if the token is invalid
     */
    public void logout(AuthToken authToken) throws DataAccessException {
        // TODO implement here
    }
}
