package dataAccess;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.*;
import models.Game;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;


/**
 * The GameDao class is responsible for accessing the {@link Game} database.
 * Currently, it uses a HashMap to store the games in memory. This will eventually
 * be replaced with a MySQL database.
 */
public class GameDao {

    private static final String INSERT = "INSERT into game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?,?)";
    private static final String FIND = "SELECT * FROM game WHERE gameID = ?";
    private static final String FIND_ALL = "SELECT * FROM game";
    private static final String CLEAR = "DELETE FROM game";
    private static final String UPDATE = "UPDATE game SET whiteUsername = ?, blackUsername = ? WHERE gameID = ?";
    private final Database db = new Database();

    /**
     * Constructor for the {@link GameDao} class, configures the database to
     * prepare to execute SQL statements
     *
     * @throws DataAccessException if there is an error accessing the database
     */
    public GameDao() throws DataAccessException {
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
                    CREATE TABLE IF NOT EXISTS game (
                        gameID INT NOT NULL,
                        whiteUsername VARCHAR(255),
                        blackUsername VARCHAR(255),
                        gameName VARCHAR(255) NOT NULL,
                        game longtext NOT NULL,
                        PRIMARY KEY (gameID),
                        FOREIGN KEY (whiteUsername) REFERENCES user(username),
                        FOREIGN KEY (blackUsername) REFERENCES user(username)
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
     * Inserts a game into the database
     *
     * @param game the {@link Game} to be inserted
     * @throws DataAccessException if there is already a game with the same id in the database
     */
    public void insert(Game game) throws DataAccessException {
        if (find(game.getGameID()) != null) {
            throw new DataAccessException("game already exists");
        }
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(INSERT)) {
            preparedStatement.setInt(1, game.getGameID());
            preparedStatement.setString(2, game.getWhiteUsername());
            preparedStatement.setString(3, game.getBlackUsername());
            preparedStatement.setString(4, game.getGameName());
            var gameJson = new Gson().toJson(game.getGame());
            preparedStatement.setString(5, gameJson);
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

    /**
     * Claims a spot in the game for the given player
     *
     * @param username the username of the player claiming the spot,
     *                 they will be inserted in either the white or black player spot.
     * @param gameID   the gameID to claim the spot in
     * @throws DataAccessException if both spots are already taken when the player tries to claim a spot
     */
    public void claimSpot(String username, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {
        var game = find(gameID);

        // check if the user only wants to spectate
        if (playerColor == null) return;

        // check if the spot is taken, if it is, throw an exception
        switch (playerColor) {
            case WHITE:
                if (game.getWhiteUsername() == null) {
                    game.setWhiteUsername(username);
                } else {
                    throw new DataAccessException("already taken");
                }
                break;
            case BLACK:
                if (game.getBlackUsername() == null) {
                    game.setBlackUsername(username);
                } else {
                    throw new DataAccessException("already taken");
                }
                break;
        }
        updatePlayers(gameID, game);
    }

    /**
     * Updates a game in the database
     *
     * @param gameID the id of the game to be updated
     * @param game   the {@link Game} to be updated
     * @throws DataAccessException if there is an error updating the game,
     *                             such as a ForeignKey constraint error the user
     *                             doesn't exist in the user table
     */
    public void updatePlayers(int gameID, models.Game game) throws DataAccessException {
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, game.getWhiteUsername());
            preparedStatement.setString(2, game.getBlackUsername());
            preparedStatement.setInt(3, gameID);
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

    /**
     * Finds a game in the database
     *
     * @param gameID the id of the game to be found
     * @return the {@link Game} with the given id or null if it does not exist
     * @throws DataAccessException if there is an error accessing the database
     */
    public models.Game find(int gameID) throws DataAccessException {
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(FIND)) {
            preparedStatement.setInt(1, gameID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new models.Game(resultSet.getInt("gameID"), resultSet.getString("whiteUsername"), resultSet.getString("blackUsername"), resultSet.getString("gameName"), jsonToGame(resultSet));
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
     * Retrieves all the games from the database
     *
     * @return an {@link ArrayList} of all games in the database
     * or an empty list if there are no games
     * @throws DataAccessException if there is an error accessing the database
     */
    public ArrayList<models.Game> findAll() throws DataAccessException {
        var conn = db.getConnection();
        try (var preparedStatement = conn.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<models.Game> games = new ArrayList<>();
            while (resultSet.next()) {
                games.add(new Game(resultSet.getInt("gameID"), resultSet.getString("whiteUsername"), resultSet.getString("blackUsername"), resultSet.getString("gameName"), jsonToGame(resultSet)));
            }
            return games;
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(conn);
        }
    }

    /**
     * Deserializes a game from the database
     *
     * @param resultSet the result set to deserialize
     * @return the deserialized game
     */
    private chess.Game jsonToGame(ResultSet resultSet) throws SQLException {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(chess.Game.class, new ChessGameAdapter());
        builder.registerTypeAdapter(chess.Board.class, new ChessBoardAdapter());
        builder.registerTypeAdapter(chess.Piece.class, new ChessPieceAdapter());
        var gson = builder.create();
        return gson.fromJson(resultSet.getString("game"), chess.Game.class);
    }

    /**
     * Clears the database of all games
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

    /**
     * Deserializer for the {@link chess.Game} class
     */
    public static class ChessGameAdapter implements JsonDeserializer<chess.Game> {

        @Override
        public chess.Game deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var jsonObject = jsonElement.getAsJsonObject();

            // give context to each element as necessary, boards are handled by ChessBoardAdapter, pieces are handled by ChessPieceAdapter
            chess.Board board = jsonDeserializationContext.deserialize(jsonObject.get("board"), chess.Board.class);
            ChessGame.TeamColor teamTurn = ChessGame.TeamColor.valueOf(jsonObject.get("teamTurn").getAsString());
            chess.Piece isEnPassant = jsonDeserializationContext.deserialize(jsonObject.get("isEnPassant"), chess.Piece.class);
            Set<chess.ChessPiece> hasMoved = jsonDeserializationContext.deserialize(jsonObject.get("hasMoved"), Set.class);
            return new chess.Game(board, teamTurn, isEnPassant, hasMoved);
        }
    }

    /**
     * Deserializer for the {@link chess.Board} class
     */
    public static class ChessBoardAdapter implements JsonDeserializer<chess.Board> {

        @Override
        public chess.Board deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var jsonObject = jsonElement.getAsJsonObject();

            // give context to the pieces in the array
            chess.Piece[][] board = jsonDeserializationContext.deserialize(jsonObject.get("board"), chess.Piece[][].class);
            return new chess.Board(board);
        }
    }

    /**
     * Deserializer for the {@link chess.Piece} class
     */
    public static class ChessPieceAdapter implements JsonDeserializer<chess.Piece> {

        @Override
        public chess.Piece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var jsonObject = jsonElement.getAsJsonObject();

            // extract all elements, return a new piece with the same elements
            ChessGame.TeamColor teamColor = ChessGame.TeamColor.valueOf(jsonObject.get("teamColor").getAsString());
            ChessPiece.PieceType pieceType = ChessPiece.PieceType.valueOf(jsonObject.get("pieceType").getAsString());
            int id = jsonObject.get("id").getAsInt();
            return new chess.Piece(teamColor, pieceType, id);
        }
    }
}
