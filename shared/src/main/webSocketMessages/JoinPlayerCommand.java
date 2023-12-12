package webSocketMessages;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayerCommand extends UserGameCommand {
    private final int gameID;
    private final String username;
    private final ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(String authToken, int gameID, String username, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.commandType = UserGameCommand.CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.username = username;
        this.playerColor = playerColor;
    }

    public int gameID() {
        return gameID;
    }

    public String username() {
        return username;
    }

    public ChessGame.TeamColor playerColor() {
        return playerColor;
    }
}
