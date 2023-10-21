package dataAccess;

import models.AuthToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The AuthTokenDao class is responsible for accessing the {@link AuthToken} database.
 * Currently, it uses a HashMap to store the authTokens in memory. It will
 * eventually be replaced with a MySQL database.
 */
public class AuthTokenDao {

    /**
     * The singleton instance of the {@link AuthTokenDao} class
     */
    private static AuthTokenDao instance;

    /**
     * The HashMap of authTokens in the database,
     * The key is the authToken string, and the value is the
     * {@link AuthToken} object associated with that authToken string
     */
    private HashMap<String, AuthToken> authTokens;

    /**
     * Private constructor for the {@link AuthTokenDao} class
     * Since it is a singleton, it should not have a public constructor
     */
    private AuthTokenDao() {
        this.authTokens = new HashMap<>();
    }

    /**
     * Gets the {@link AuthTokenDao#instance} of the {@link AuthTokenDao} class.
     * If there is no instance, it creates one through the private {@link AuthTokenDao#AuthTokenDao()} constructor
     *
     * @return the {@link AuthTokenDao#instance} of the {@link AuthTokenDao} class
     */
    public static AuthTokenDao getInstance() {
        if (instance == null) {
            instance = new AuthTokenDao();
        }
        return instance;
    }

    /**
     * Inserts an authToken into the database
     *
     * @param authToken the {@link AuthToken} to be inserted, which contains the token and username
     * @throws DataAccessException if there is already an authToken with the same username in the database,
     *                             or if there is another error
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        authTokens.put(authToken.getAuthToken(), authToken);
    }

    /**
     * Deletes an authToken from the database
     *
     * @param token the string value of the {@link AuthToken} to be deleted
     * @throws DataAccessException if there is no authToken associated with the same username in the database,
     *                             or if there is another error
     */
    public void delete(String token) throws DataAccessException {
        if (!authTokens.containsKey(token)) {
            throw new DataAccessException("Invalid token");
        }
        authTokens.remove(token);
    }

    /**
     * Finds an authToken in the database
     *
     * @param token the token string of the authToken to be found
     * @return the {@link AuthToken} associated with the correct username or null if it doesn't exist
     * @throws DataAccessException if there is an error accessing the database
     */
    public AuthToken find(String token) throws DataAccessException {
        //TODO: implement
        return null;
    }

    /**
     * Finds all authTokens in the database
     *
     * @return an ArrayList of all the authTokens in the database
     * @throws DataAccessException if there is an error accessing the database
     */
    public ArrayList<AuthToken> findAll() throws DataAccessException {
        // TODO: implement
        return null;
    }

    /**
     * Clears all authTokens from the database
     *
     * @throws DataAccessException if there is an error accessing the database
     */
    public void clear() throws DataAccessException {
        authTokens.clear();
    }


}
