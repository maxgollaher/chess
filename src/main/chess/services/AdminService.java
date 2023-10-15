package chess.services;

import chess.dataAccess.*;
import chess.models.*;
import dataAccess.DataAccessException;

/**
 * The Admin Service class handles all requests to the /db endpoint of the API.
 * It interacts with the {@link AuthTokenDao}, {@link GameDao}, and {@link UserDao} to
 * clear and manage the databases. It is used to manage the DAOs as a whole.
 */
public class AdminService {

    /**
     * The {@link AuthTokenDao} to be used to access the {@link AuthToken} database
     */
    private AuthTokenDao authTokenDao;

    /**
     * The {@link GameDao} to be used to access the {@link Game} database
     */
    private GameDao gameDao;

    /**
     * The {@link UserDao} to be used to access the {@link User} database
     */
    private UserDao userDao;

    /**
     * Clears all data from the database. This is used for testing purposes, and no
     * authentication is required.
     * @throws DataAccessException if there is an error accessing the database
     */
    public void clearDatabase() throws DataAccessException {
        // TODO implement here
    }
}
