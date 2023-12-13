package webSocketMessages;

import chess.ChessMove;
import webSocketMessages.userCommands.UserGameCommand;

public class MoveCommand extends UserGameCommand {
    private final int gameID;
    private final ChessMove move;

    public MoveCommand(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }

    public int gameID() {
        return gameID;
    }

    public ChessMove move() {
        return move;
    }
}
