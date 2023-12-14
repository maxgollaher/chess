package ui.websocket;

import chess.ChessGame;
import chess.ChessMove;
import webSocketMessages.ErrorMessage;
import webSocketMessages.LoadGameMessage;
import webSocketMessages.Notification;

import java.util.Collection;

public interface NotificationHandler {
    void notify(Notification serverMessage);
    void loadGame(LoadGameMessage loadGameMessage);
    void highlightMoves(Collection<ChessMove> moves, ChessGame game);
    void error(ErrorMessage errorMessage);
    void drawBoard(String board);
}
