package services;

import dataAccess.AuthTokenDao;
import dataAccess.DataAccessException;
import dataAccess.UserDao;
import models.AuthToken;
import models.User;
import requests.RegisterRequest;
import responses.LoginResponse;

/**
 * Interacts with the {@link UserDao} to register new users. It handles all requests
 * to the /user endpoint of the API.
 */
public class UserService {

    /**
     * The {@link UserDao} to be used to access the {@link User} database
     */
    private static final UserDao userDao;

    /**
     * The {@link AuthTokenDao} to be used to access the {@link AuthToken} database
     */
    private static final AuthTokenDao authTokenDao;

    static {
        try {
            authTokenDao = new AuthTokenDao();
            userDao = new UserDao();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registers a new user based on a given RegisterRequest
     *
     * @param request a {@link RegisterRequest} to register a new user, which contains
     *                the username, password, and email
     * @return a {@link LoginResponse} containing the username and {@link AuthToken}
     * @throws DataAccessException if there is an error accessing the database or if the
     *                             username/email is already taken.
     */
    public LoginResponse register(RegisterRequest request) throws DataAccessException {
        var user = new User(request.username(), request.password(), request.email());
        userDao.insert(user);
        var authToken = new AuthToken(user.getUsername());
        authTokenDao.insert(authToken);
        return new LoginResponse(authToken.getUsername(), authToken.getAuthToken());
    }
}
