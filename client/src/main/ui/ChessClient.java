package ui;

import chess.*;
import exception.ResponseException;
import models.Game;
import server.ServerFacade;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.util.ArrayList;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class ChessClient {

    private final ServerFacade server;
    private final String serverURl;
    private final NotificationHandler notificationHandler;
    protected ChessGame.TeamColor teamColor;
    private State state = State.SIGNED_OUT;
    private String authToken;
    private String username;
    private ArrayList<Game> games;
    private WebSocketFacade ws;
    private int gameID;

    private chess.ChessGame game;
    private String board;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) throws ResponseException {
        this.server = new ServerFacade(serverUrl);
        this.serverURl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public void game(models.Game game) {
        this.game = game.getGame();
    }

    public void board(String board) {
        this.board = board;
    }

    /**
     * Evaluates the input and returns the result.
     */
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
                case "redraw" -> redraw();
                case "leave" -> leaveGame();
                case "make" -> makeMove(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                case "quit" -> "quit";
                case "clear" -> clear(); // for testing, clears the database
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String highlight(String[] params) throws ResponseException {
        assertWebSocket();
        var position = parsePosition(params[0]);
        var moves = game.validMoves(position);
        notificationHandler.highlightMoves(moves, game);
        return "";
    }

    private String resign() throws ResponseException {
        assertWebSocket();
        ws.resign(authToken, username);
        return "Resigned";
    }

    private String makeMove(String[] params) throws ResponseException {
        assertWebSocket();
        var move = parseMove(params);
        ws.makeMove(authToken, gameID, move);
        return "";
    }

    private ChessMove parseMove(String[] params) throws ResponseException {
        if (params.length < 1) {
            throw new ResponseException(400, "Expected: <MOVE>");
        }
        var moveString = params[0];
        var from = parsePosition(moveString.substring(0, 2));
        var to = parsePosition(moveString.substring(2, 4));
        var promotion = (moveString.length() > 4) ?
                ChessPiece.PieceType.valueOf(moveString.substring(4).toUpperCase()) : null;
        return new Move(from, to, promotion);
    }

    private Position parsePosition(String position) throws ResponseException {
        position = position.toLowerCase();
        var col = position.charAt(0) - 'a' + 1;
        var row = Character.getNumericValue(position.charAt(1));
        return new Position(row, col);
    }

    private String leaveGame() throws ResponseException {
        assertWebSocket();
        state = State.SIGNED_IN;
        ws.leaveGame(authToken, username);
        ws = null;
        return "Left game";
    }

    private void assertWebSocket() throws ResponseException {
        if (ws == null) {
            throw new ResponseException(400, "You must join a game first");
        }
    }

    /*
     * Sends a redraw request to the Repl
     */
    private String redraw() {
        notificationHandler.drawBoard(board);
        return "";
    }

    private String clear() throws ResponseException {
        server.clear();
        return "Cleared database";
    }

    /**
     * Registers a new user.
     */
    private String register(String[] params) throws ResponseException {
        if (params.length >= 3) {
            var response = server.register(params);
            authToken = response.authToken();
            state = State.SIGNED_IN;
            username = response.username();
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
            username = response.username();
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
        username = null;
        return "goodbye 👋";
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNED_OUT) {
            throw new ResponseException(400, "You must sign in");
        } else if (state == State.IN_GAME) {
            throw new ResponseException(400, "You must first leave the game");
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
        this.gameID = parseGameID(params[0]);
        ChessGame.TeamColor playerColor = getPlayerColor(params);
        this.teamColor = playerColor;
        server.joinGame(gameID, playerColor, authToken);
        if (playerColor == null) {
            state = State.OBSERVING;
        } else {
            state = State.IN_GAME;
        }

        this.ws = new WebSocketFacade(serverURl, notificationHandler);
        ws.joinPlayer(gameID, playerColor, username, authToken);
        return String.format("Joined game with ID: %d", gameID);
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

    public String help() {
        if (state == State.SIGNED_OUT) {
            return String.format("""
                    %sregister <USERNAME> <PASSWORD> <EMAIL> - %s to create an account
                    %slogin <USERNAME> <PASSWORD> - %s to play chess
                    %squit - %s playing chess
                    %shelp - %s with possible commands""", BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY) + RESET_TEXT_COLOR;
        } else if (state == State.SIGNED_IN) {
            return String.format("""
                    %screate <NAME> - %s a game
                    %slist - %s games
                    %sjoin <ID> [WHITE|BLACK|<empty>] - %s a game
                    %sobserve <ID> - %s a game
                    %slogout - %s when you are done
                    %squit - %s playing chess
                    %shelp - %s with possible commands""", BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY) + RESET_TEXT_COLOR;
        } else if (state == State.IN_GAME) {
            return String.format("""
                    %sredraw - %s the chess board
                    %sleave - %s the game
                    %smake <MOVE> - %s a move
                    %sresign - %s the game
                    %shighlight <POSITION> - %s highlights legal moves at a position
                    %squit - %s the program
                    %shelp - %s with possible commands""", BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY) + RESET_TEXT_COLOR;

        } else {
            return String.format("""
                    %sredraw - %s the chess board
                    %sleave - %s the game
                    %squit - %s the program
                    %shelp - %s with possible commands""", BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY, BLUE, LIGHT_GREY) + RESET_TEXT_COLOR;
        }
    }

    public State getState() {
        return state;
    }
}
