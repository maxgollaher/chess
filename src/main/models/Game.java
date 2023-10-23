package models;

import chess.ChessGame;
import java.util.Objects;

/**
 * Represents a game. Contains the gameID, the usernames of the players, the name of the game, and the game itself.
 */
public class Game {

    /**
     * The id of the game.
     */
    private int gameID;

    /**
     * The username of the white player.
     */
    private String whiteUsername;

    /**
     * The username of the black player.
     */
    private String blackUsername;

    /**
     * The name of the game.
     */
    private String gameName;

    /**
     * The game object itself.
     *
     * @see ChessGame
     */
    private ChessGame game;

    /**
     * The static counter for the gameID.
     */
    private static int counter = 1;


    /**
     * Blank constructor for Game, used to create null Game objects if needed.
     */
    public Game() {
    }

    /**
     * Creates a new Game object.
     *
     * @param gameName the name of the game.
     */
    public Game(String gameName) {
        this.gameName = gameName;
        this.gameID = counter++;
        this.game = new chess.Game();
    }

    /**
     * Creates a new Game object.
     *
     * @param gameID        the id of the game.
     * @param whiteUsername the username of the white player.
     * @param blackUsername the username of the black player.
     * @param gameName      the name of the game.
     * @param game          the {@link ChessGame} object itself.
     */

    public Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game1 = (Game) o;
        return gameID == game1.gameID && Objects.equals(whiteUsername, game1.whiteUsername) && Objects.equals(blackUsername, game1.blackUsername) && Objects.equals(gameName, game1.gameName) && Objects.equals(game, game1.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameID: " + gameID +
                ", whiteUsername: '" + whiteUsername + '\'' +
                ", blackUsername: '" + blackUsername + '\'' +
                ", gameName: '" + gameName + '\'' +
                ", game: " + game +
                '}';
    }
}
