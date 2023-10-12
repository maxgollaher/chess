package chess.services;

import chess.dataAccess.AuthTokenDao;
import chess.dataAccess.GameDao;
import chess.dataAccess.UserDao;
import dataAccess.DataAccessException;

public class AdminService {

    private AuthTokenDao authTokenDao;
    private GameDao gameDao;
    private UserDao userDao;


    public void clearDatabase(String authToken) throws DataAccessException {
        // TODO implement here
    }
}
