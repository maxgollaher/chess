package ui;

import chess.ChessGame;
import chess.ChessPiece;
import models.ModelSerializer;
import ui.websocket.NotificationHandler;
import webSocketMessages.LoadGameMessage;
import webSocketMessages.Notification;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {

    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.printf("%s%s👑 Welcome to 240 chess. Type Help to get started 👑%n", WHITE, BOLD);
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + WHITE + "[" + client.getState().toString() + "]" + ">>> " + GREEN);
    }

    @Override
    public void notify(Notification serverMessage) {
        System.out.print(BLUE + "\n" + serverMessage.message());
        printPrompt();
    }

    @Override
    public void loadGame(LoadGameMessage loadGameMessage) {
        models.Game game = ModelSerializer.deserialize(loadGameMessage.message(), models.Game.class);
        var gameID = game.getGameID();
        System.out.println(getJoinGameBoard(game.getGame(), gameID, client.teamColor));
        printPrompt();
    }

    private String getJoinGameBoard(ChessGame game, int gameID, ChessGame.TeamColor playerColor) {
        var sb = new StringBuilder();
        var chessBoard = game.getBoard();
        sb.append("Joined game with ID: ").append(gameID).append("\n");
        if (playerColor == ChessGame.TeamColor.BLACK) {
            sb.append(printBoard(chessBoard, ChessGame.TeamColor.BLACK)).append("\n\n");
        } else {
            sb.append(printBoard(chessBoard, ChessGame.TeamColor.WHITE)).append("\n\n");
        }
        return sb.toString();
    }

    private String printBoard(chess.ChessBoard board, ChessGame.TeamColor playerColor) {
        var sb = new StringBuilder();
        var currentBG = playerColor == ChessGame.TeamColor.WHITE ? BG_WHITE : BG_BLACK; // a1 is black
        var letters = playerColor == ChessGame.TeamColor.WHITE ? "    a  b  c  d  e  f  g  h    " : "    h  g  f  e  d  c  b  a    ";
        sb.append(BG_LIGHT_GRAY + BLACK + BOLD).append(letters).append(RESET_BG_COLOR).append('\n');

        int startRow = playerColor == ChessGame.TeamColor.WHITE ? 8 : 1;
        int endRow = playerColor == ChessGame.TeamColor.WHITE ? 0 : 9;
        int rowIncrement = playerColor == ChessGame.TeamColor.WHITE ? -1 : 1;

        for (int i = startRow; i != endRow; i += rowIncrement) {
            sb.append(BG_LIGHT_GRAY + BLACK + BOLD).append(" ").append(i).append(" ").append(RESET_BG_COLOR);
            for (int j = 1; j <= 8; j++) {
                sb.append(currentBG);
                var position = playerColor == ChessGame.TeamColor.WHITE ? new chess.Position(i, j) : new chess.Position(i, 9 - j);
                var piece = board.getPiece(position);
                var pieceString = piece == null ? "   " : pieceToUnicode(piece);
                sb.append(BOLD).append(pieceString);
                currentBG = (currentBG.equals(BG_BLACK)) ? BG_WHITE : BG_BLACK;
            }
            sb.append(BG_LIGHT_GRAY + BLACK + BOLD).append(" ").append(i).append(" ").append(RESET_BG_COLOR).append('\n');
            currentBG = (currentBG.equals(BG_BLACK)) ? BG_WHITE : BG_BLACK;
        }

        sb.append(BG_LIGHT_GRAY + BLACK + BOLD).append(letters).append(RESET_BG_COLOR);
        return sb.toString();
    }

    private String pieceToUnicode(ChessPiece piece) {
        return switch (piece.getTeamColor()) {
            case WHITE -> getPieceString(piece, RED);
            case BLACK -> getPieceString(piece, BLUE);
        };
    }

    private String getPieceString(ChessPiece piece, String textColor) {
        return textColor + switch (piece.getPieceType()) {
            case PAWN -> " P ";
            case ROOK -> " R ";
            case KNIGHT -> " N ";
            case BISHOP -> " B ";
            case QUEEN -> " Q ";
            case KING -> " K ";
        };
    }

}
