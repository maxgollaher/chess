package dataAccess;

import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The UserDao class is responsible for accessing the {@link User} database.
 * Currently, it uses a MySQL database to store the user data.
 */
public class UserDao {

    private static final String INSERT = "INSERT into user (username, password, email) VALUE (?,?,?)";
    private static final String FIND = "SELECT * FROM user WHERE username = ?";
    private static final String FIND_ALL = "SELECT * FROM user";
    private static final String CLEAR = "DELETE FROM user";
    private final Database db = new Database();

    /**
     * Constructor for the {@link UserDao} class, configures the database to
     * prepare to execute SQL statements
     *
     * @throws DataAccessException if there is an error accessing the database
     */
    public UserDao() throws DataAccessException {
        configureDatabase();
    }

    /**
     * Configures the database to prepare to execute SQL statements
     *
     * @throws DataAccessException when there is a problem accessing the database
     *                             or creating the table
     */
    private void configureDatabase() throws DataAccessException {
        try (var conn = db.getConnection()) {
            conn.setCatalog(Database.DB_NAME);
            var createUserTable = """
                    CREATE TABLE IF NOT EXISTS user (
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        PRIMARY KEY (username)
                    )""";

            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException(ex.toString());
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }
    }

    /**
     * Inserts a user into the database
     *
     * @param user the {@link User} to be inserted
     * @throws DataAccessException if there is already a user with the same username in the database,
     *                             or if there is another error
     */
    public void insert(User user) throws DataAccessException {
        if (find(user.getUsername()) != null) {
            throw new DataAccessException("already taken");
        }
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(INSERT)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

    /**
     * Finds a user in the database
     *
     * @param username the username of the user to be found
     * @return the {@link User} with the given username, or null if not found
     * @throws DataAccessException if there is an error accessing the database.
     */
    public User find(String username) throws DataAccessException {
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(FIND)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email")
                );
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

    /**
     * Finds all users in the database
     *
     * @return an ArrayList of all the users in the database
     * @throws DataAccessException if there is an error accessing the database.
     */
    public ArrayList<User> findAll() throws DataAccessException {
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email")
                ));
            }
            return users;
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

    /**
     * Clears all users from the database
     *
     * @throws DataAccessException if there is an error accessing the database.
     */
    public void clear() throws DataAccessException {
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(CLEAR)) {
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

}
