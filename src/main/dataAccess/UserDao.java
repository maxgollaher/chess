package dataAccess;

import models.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The UserDao class is responsible for accessing the {@link User} database.
 * Currently, it uses a HashMap to store the users in memory. It will
 * eventually be replaced with a MySQL database.
 */
public class UserDao {

    /**
     * The singleton instance of the {@link UserDao} class
     */
    private static UserDao instance;

    /**
     * The HashMap of games in the database
     * The key is the username, and the value is the {@link User} object
     */
    private HashMap<String, User> users;

    /**
     * Private constructor for the {@link UserDao} class
     * Since it is a singleton, it should not have a public constructor
     * It creates the {@link UserDao#users} HashMap
     */
    private UserDao() {
        this.users = new HashMap<>();
    }

    /**
     * Gets the {@link UserDao#instance} of the {@link UserDao} class.
     * If there is no instance, it creates one through the private {@link UserDao#UserDao()} constructor
     *
     * @return the {@link UserDao#instance} of the {@link UserDao} class
     */
    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    /**
     * Inserts a user into the database
     *
     * @param user the {@link User} to be inserted
     * @throws DataAccessException if there is already a user with the same username in the database,
     *                             or if there is another error
     */
    public void insert(User user) throws DataAccessException {
        if (users.containsKey(user.getUsername())) {
            throw new DataAccessException("already taken");
        }
        users.put(user.getUsername(), user);
    }


    /**
     * Updates a user in the database
     *
     * @param username the username of the user to be updated
     * @param user     the {@link User} to be updated
     * @throws DataAccessException if there is no user with the given username in the database,
     *                             or if there is another error
     */
    public void update(String username, User user) throws DataAccessException {
        if (!users.containsKey(username)) {
            throw new DataAccessException("User not found");
        }
        users.put(username, user);
    }

    /**
     * Deletes a user from the database
     *
     * @param username the username of the user to be deleted
     * @throws DataAccessException if there is no user with the given username in the database,
     *                             or if there is another error
     */
    public void delete(String username) throws DataAccessException {
        if (!users.containsKey(username)) {
            throw new DataAccessException("User not found");
        }
        users.remove(username);
    }

    /**
     * Finds a user in the database
     *
     * @param username the username of the user to be found
     * @return the {@link User} with the given username, or null if not found
     * @throws DataAccessException if there is an error accessing the database.
     */
    public User find(String username) throws DataAccessException {
        return users.get(username);
    }

    /**
     * Finds all users in the database
     *
     * @return an ArrayList of all the users in the database
     * @throws DataAccessException if there is an error accessing the database.
     */
    public ArrayList<User> findAll() throws DataAccessException {
        // TODO: implement
        return null;
    }

    /**
     * Clears all users from the database
     *
     * @throws DataAccessException if there is an error accessing the database.
     */
    public void clear() throws DataAccessException {
        users.clear();
    }

}
