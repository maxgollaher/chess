package chess.dataAccess;

import chess.models.Game;
import dataAccess.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The GameDao class is responsible for accessing the {@link Game} database.
 * Currently, it uses a HashMap to store the games in memory. This will eventually
 * be replaced with a MySQL database.
 */
public class GameDao {

    /**
     * The singleton instance of the {@link GameDao} class
     */
    private static GameDao instance;

    /**
     * The HashMap of games in the database
     * The key is the gameId, and the value is the {@link Game} object
     */
    private HashMap<Integer, Game> games;

    /**
     * Private constructor for the {@link GameDao} class
     * Since it is a singleton, it should not have a public constructor
     * It creates the {@link GameDao#games} HashMap
     */
    private GameDao() {
        this.games = new HashMap<>();
    }

    /**
     * Gets the {@link GameDao#instance} of the {@link GameDao} class.
     * If there is no instance, it creates one through the private constructor
     *
     * @return the {@link GameDao#instance} of the {@link GameDao} class
     */
    public static GameDao getInstance() {
        if (instance == null) {
            instance = new GameDao();
        }
        return instance;
    }

    /**
     * Inserts a game into the database
     *
     * @param game the {@link Game} to be inserted
     * @throws DataAccessException if there is already a game with the same id in the database
     */
    public void insert(Game game) throws DataAccessException {
        // TODO: implement
    }

    /**
     * Updates a game in the database
     *
     * @param gameId the id of the game to be updated
     * @param game   the updated {@link Game} to replace the existing one in the database
     * @throws DataAccessException if there is no game with the given id in the database
     */
    public void update(int gameId, Game game) throws DataAccessException {
        // TODO: implement
    }

    /**
     * Claims a spot in the game for the given player
     *
     * @param username the username of the player claiming the spot,
     *                 they will be inserted in either the white or black player spot.
     * @param game     the {@link Game} to claim the spot in
     * @throws DataAccessException if both spots are already taken
     */
    public void claimSpot(String username, Game game) throws DataAccessException {
        // TODO: implement
    }

    /**
     * Deletes a game from the database
     *
     * @param gameId the gameId of the {@link Game} to be deleted
     * @throws DataAccessException if there is no game with the given id in the database
     */
    public void delete(int gameId) throws DataAccessException {
        // TODO: implement
    }

    /**
     * Finds a game in the database
     *
     * @param gameId the id of the game to be found
     * @return the {@link Game} with the given id or null if it does not exist
     * @throws DataAccessException if there is an error accessing the database
     */
    public Game find(int gameId) throws DataAccessException {
        // TODO: implement
        return null;
    }

    /**
     * Retrieves all the games from the database
     *
     * @return an {@link ArrayList} of all games in the database
     * or an empty list if there are no games
     * @throws DataAccessException if there is an error accessing the database
     */
    public ArrayList<Game> findAll() throws DataAccessException {
        // TODO: implement
        return null;
    }

    /**
     * Clears the database of all games
     *
     * @throws DataAccessException if there is an error accessing the database
     */
    public void clear() throws DataAccessException {
        // TODO: implement
    }


}
