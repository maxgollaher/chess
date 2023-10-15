package chess.dataAccess;

import chess.models.Game;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Game Data Access Object
 * This class is responsible for accessing the Game objects in the database
 */
public class GameDao {
    private static GameDao instance;
    private HashMap<Integer, Game> games;

    /**
     * Private constructor for the @GameDao class
     * Since it is a singleton, it should not have a public constructor
     * It creates the @games HashMap
     */
    private GameDao() {
        this.games = new HashMap<>();
    }

    /**
     * Gets the instance of the @GameDao class.
     * If there is no instance, it creates one through the private constructor
     * @return the instance of the @GameDao class
     */
    public static GameDao getInstance() {
        if (instance == null) {
            instance = new GameDao();
        }
        return instance;
    }

    /**
     * Inserts a game into the database
     * @param game the game to be inserted
     */
    public void insert(Game game) {
        // TODO: implement
    }

    /**
     * Updates a game in the database
     * @param game the game to be updated
     */
    public void update(Game game) {
        // TODO: implement
    }

    /**
     * Claims a spot in the game for the given player
     * @param username the username of the player claiming the spot,
     *                 they will be inserted in either the white or black player spot.
     * @param game the game to claim the spot in
     */
    public void claimSpot(String username, Game game) {
        // TODO: implement
    }

    /**
     * Deletes a game from the database
     * @param game the game to be deleted
     */
    public void delete(Game game) {
        // TODO: implement
    }

    /**
     * Finds a game in the database
     * @param gameId the id of the game to be found
     * @return the game with the given id
     */
    public Game find(int gameId) {
        // TODO: implement
        return null;
    }

    /**
     * Retrieves all the games from the database
     * @return an @ArrayList of all games in the database
     */
    public ArrayList<Game> findAll() {
        // TODO: implement
        return null;
    }

    /**
     * Clears the database
     */
    public void clear() {
         // TODO: implement
    }



}
