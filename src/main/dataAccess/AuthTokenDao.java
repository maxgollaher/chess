package dataAccess;

import models.AuthToken;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The AuthTokenDao class is responsible for accessing the {@link AuthToken} database.
 * Currently, it uses a MySQL database to store the authTokens.
 */
public class AuthTokenDao {

    private static final String INSERT = "INSERT into authToken (authToken, username) VALUES (?,?)";
    private static final String DELETE = "DELETE FROM authToken WHERE authToken = ?";
    private static final String FIND = "SELECT * FROM authToken WHERE authToken = ?";
    private static final String FIND_ALL = "SELECT * FROM authToken";
    private static final String CLEAR = "DELETE FROM authToken";
    private final Database db = new Database();

    /**
     * Constructor for the {@link AuthTokenDao} class, configures the database to
     * prepare to execute SQL statements
     *
     * @throws DataAccessException if there is an error accessing the database
     */
    public AuthTokenDao() throws DataAccessException {
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
            var createAuthTable = """
                    CREATE TABLE IF NOT EXISTS authToken (
                        authToken VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        PRIMARY KEY (authToken),
                        FOREIGN KEY (username) REFERENCES user(username)
                    )""";

            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException(ex.toString());
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }
    }

    /**
     * Inserts an authToken into the database
     *
     * @param authToken the {@link AuthToken} to be inserted, which contains the token and username
     * @throws DataAccessException if there is already an authToken with the same username in the database,
     *                             or if there is another error
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(INSERT)) {
            preparedStatement.setString(1, authToken.getAuthToken());
            preparedStatement.setString(2, authToken.getUsername());
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

    /**
     * Deletes an authToken from the database
     *
     * @param token the string value of the {@link AuthToken} to be deleted
     * @throws DataAccessException if there is no authToken associated with the same username in the database,
     *                             or if there is another error
     */
    public void delete(String token) throws DataAccessException {
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(DELETE)) {
            preparedStatement.setString(1, token);
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

    /**
     * Finds an authToken in the database
     *
     * @param token the token string of the authToken to be found
     * @return the {@link AuthToken} associated with the correct username or null if it doesn't exist
     * @throws DataAccessException if there is an error accessing the database
     */
    public AuthToken find(String token) throws DataAccessException {
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(FIND)) {
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            if (resultSet.next()) {
                return new AuthToken(resultSet.getString("authToken"), resultSet.getString("username"));
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
     * Finds all authTokens in the database
     *
     * @return an ArrayList of all the authTokens in the database
     * @throws DataAccessException if there is an error accessing the database
     */
    public ArrayList<AuthToken> findAll() throws DataAccessException {
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<AuthToken> authTokens = new ArrayList<>();
            while (resultSet.next()) {
                authTokens.add(new AuthToken(resultSet.getString("authToken"), resultSet.getString("username")));
            }
            return authTokens;
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

    /**
     * Clears all authTokens from the database
     *
     * @throws DataAccessException if there is an error accessing the database
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

