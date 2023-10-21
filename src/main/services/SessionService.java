package services;

import dataAccess.AuthTokenDao;
import dataAccess.UserDao;
import models.AuthToken;
import models.User;
import services.requests.LoginRequest;
import services.responses.LoginResponse;
import dataAccess.DataAccessException;

/**
 * This class is responsible for handling all requests to the session
 * endpoint of the API. It interacts with the {@link AuthTokenDao} to login and logout
 * users.
 */
public class SessionService {

    /**
     * The {@link AuthTokenDao} to be used to access the {@link AuthToken} database
     */
    private AuthTokenDao authTokenDao = AuthTokenDao.getInstance();

    /**
     * The {@link UserDao} to be used to access the {@link User} database
     */
    private UserDao userDao = UserDao.getInstance();

    /**
     * Logs in a user based on a given {@link LoginRequest}
     *
     * @param request a {@link LoginRequest} to log in a user, which contains
     *                the username and password.
     * @return a {@link LoginResponse} object containing the username and {@link AuthToken}
     * @throws DataAccessException if there is an error accessing the database or if the
     *                             username/password is incorrect
     */
    public LoginResponse login(LoginRequest request) throws DataAccessException {
        var foundUser = userDao.find(request.getUsername());
        if (foundUser == null || !foundUser.getPassword().equals(request.getPassword())) {
            throw new DataAccessException("Username or password is incorrect");
        }
        var authToken = new AuthToken(request.getUsername());
        authTokenDao.insert(authToken);
        return new LoginResponse(authToken.getUsername(), authToken.getAuthToken());
    }

    /**
     * Logs out a user based on a given token
     *
     * @param authToken the {@link AuthToken} of the user to logout
     * @throws DataAccessException if there is an error accessing the database
     *                             or if the token is invalid
     */
    public void logout(String authToken) throws DataAccessException {
        authTokenDao.delete(authToken);
    }
}