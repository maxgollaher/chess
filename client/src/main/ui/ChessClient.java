package ui;

import exception.ResponseException;
import server.ServerFacade;
import static ui.EscapeSequences.*;

import java.util.Arrays;

public class ChessClient {

    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNED_OUT;

    public ChessClient(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
                case "join" -> joinGame(params);
                case "observe" -> joinGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        if (state == State.SIGNED_OUT) {
            return BLUE + String.format("""
                register <USERNAME> <PASSWORD> <EMAIL> - %s to create an account
                Login <USERNAME> <PASSWORD> - %s to play chess
                quit - %s playing chess
                help - %s with possible commands
                """, LIGHT_GREY, LIGHT_GREY, LIGHT_GREY, LIGHT_GREY) + RESET_TEXT_COLOR;
        } else {
            return BLUE + String.format("""
                create <NAME> - %s a game
                list - %s games
                join <ID> [WHITE|BLACK|<empty>] - %s a game
                observe <ID> - %s a game
                logout - %s when you are done
                quit - %s playing chess
                help - %s with possible commands
                """, LIGHT_GREY, LIGHT_GREY, LIGHT_GREY, LIGHT_GREY, LIGHT_GREY, LIGHT_GREY, LIGHT_GREY) + RESET_TEXT_COLOR;
        }
    }

    private String joinGame(String[] params) throws ResponseException {
        assertSignedIn();
        return null;
    }

    private String listGames() throws ResponseException {
        assertSignedIn();
        return null;
    }

    private String createGame(String[] params) throws ResponseException {
        assertSignedIn();
        return null;
    }

    private String logout() throws ResponseException {
        assertSignedIn();
        return null;
    }

    private String login(String[] params) {
        return null;
    }

    private String register(String[] params) {
        return null;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNED_OUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
