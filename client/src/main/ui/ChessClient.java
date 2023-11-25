package ui;

import chess.ChessGame;
import chess.ChessPiece;
import exception.ResponseException;
import models.Game;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class ChessClient {

    private final ServerFacade server;
    private State state = State.SIGNED_OUT;
    private String authToken;
    private ArrayList<Game> games;

    public ChessClient(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join", "observe" -> joinGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    /**
     * Registers a new user.
     */
    private String register(String[] params) throws ResponseException {
        if (params.length >= 3) {
            var response = server.register(params);
            authToken = response.authToken();
            state = State.SIGNED_IN;
            return String.format("Registered as %s!", response.username());
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    /**
     * Logs in a user.
     */
    private String login(String[] params) throws ResponseException {
        if (params.length >= 2) {
            var response = server.login(params);
            authToken = response.authToken();
            state = State.SIGNED_IN;
            return String.format("Logged in as %s!", response.username());
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    /**
     * Logs out a user. Deletes the auth token.
     */
    private String logout() throws ResponseException {
        assertSignedIn();
        server.logout(authToken);
        state = State.SIGNED_OUT;
        authToken = null;
        return "goodbye 👋";
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNED_OUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

    /**
     * Creates a new game.
     */
    private String createGame(String[] params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            var response = server.createGame(params, authToken);
            return String.format("Created game %s!", response.gameID());
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    /**
     * Lists all games.
     */
    private String listGames() throws ResponseException {
        assertSignedIn();
        var response = server.listGames(authToken);
        games = response.games();
        if (games.isEmpty()) {
            return "No games found";
        }
        var sb = new StringBuilder("Games:\n");
        int i = 1;
        for (var game : games) {
            sb.append(String.format(" %d. %s\n\tWhite: %s\n\tBlack: %s\n", i++, game.getGameName(), game.getWhiteUsername(), game.getBlackUsername()));
        }
        return sb.toString();
    }

    private String joinGame(String[] params) throws ResponseException {
        assertSignedIn();
        if (params.length < 1) {
            throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK|<empty>]");
        }
        int gameID = parseGameID(params[0]);
        ChessGame.TeamColor playerColor = getPlayerColor(params);
        server.joinGame(gameID, playerColor, authToken);

        // TODO: connect to websocket using another joinGame method to get the actual game board
        return getJoinGameBoard(gameID);
    }

    private int parseGameID(String param) throws ResponseException {
        try {
            var gameID = Integer.parseInt(param);
            if (games != null && !games.isEmpty()) {
                gameID = games.get(gameID - 1).getGameID();
            }
            return gameID;
        } catch (NumberFormatException ex) {
            throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK|<empty>]");
        }
    }

    private static ChessGame.TeamColor getPlayerColor(String[] params) throws ResponseException {
        return (params.length > 1) ? switch (params[1].toUpperCase()) {
            case "WHITE" -> ChessGame.TeamColor.WHITE;
            case "BLACK" -> ChessGame.TeamColor.BLACK;
            default -> throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK|<empty>]");
        } : null;
    }

    private String getJoinGameBoard(int gameID) {
        var sb = new StringBuilder();
        var tempGame = new chess.Game();
        var chessBoard = tempGame.getBoard();
        sb.append("Joined game with ID: ").append(gameID).append("\n");
        sb.append(printBoard(chessBoard, ChessGame.TeamColor.WHITE)).append("\n\n");
        sb.append(printBoard(chessBoard, ChessGame.TeamColor.BLACK));
        return sb.toString();
    }

    private String printBoard(chess.ChessBoard board, ChessGame.TeamColor playerColor) {
        var sb = new StringBuilder();
        var currentBG = BG_BLACK;
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

    public String help() {
        if (state == State.SIGNED_OUT) {
            return String.format("""
                    %sregister <USERNAME> <PASSWORD> <EMAIL> - %s to create an account
                    %slogin <USERNAME> <PASSWORD> - %s to play chess
                    %squit - %s playing chess
                    %shelp - %s with possible commands""", BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY) + RESET_TEXT_COLOR;
        } else {
            return String.format("""
                    %screate <NAME> - %s a game
                    %slist - %s games
                    %sjoin <ID> [WHITE|BLACK|<empty>] - %s a game
                    %sobserve <ID> - %s a game
                    %slogout - %s when you are done
                    %squit - %s playing chess
                    %shelp - %s with possible commands""", BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY) + RESET_TEXT_COLOR;
        }
    }

    public State getState() {
        return state;
    }
}
