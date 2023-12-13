package server.websocket;


import chess.ChessGame;
import chess.Game;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import models.ModelSerializer;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import services.GameService;
import services.SessionService;
import webSocketMessages.*;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private static final GameService gameService = new GameService();
    private static final SessionService sessionService = new SessionService();
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        try {
            var conn = getConnection(command.getAuthString(), session);
            if (conn != null) {
                switch (command.getCommandType()) {
                    case JOIN_PLAYER, JOIN_OBSERVER -> join(conn, message);
                    case MAKE_MOVE -> move(conn, message);
                    case LEAVE -> leave(conn, message);
                    case RESIGN -> resign(conn, message);
                }
            } else {
                Connection.sendError(session.getRemote(), "You are not connected to a game");
            }
        } catch (Exception e) {
            Connection.sendError(session.getRemote(), e.getMessage());
        }
    }

    private void move(Connection conn, String message) throws InvalidMoveException, IOException, DataAccessException {
        var moveCommand = ModelSerializer.deserialize(message, MoveCommand.class);
        var authToken = moveCommand.getAuthString();
        var move = moveCommand.move();
        var gameID = moveCommand.gameID();
        var game = conn.game;
        if (conn.game == null || gameID != conn.game.getGameID()) {
            throw new RuntimeException("You are not connected to this game");
        }
        var expectedPlayer = game.getGame().getTeamTurn() == ChessGame.TeamColor.WHITE ? game.getWhiteUsername() : game.getBlackUsername();
        var actualPlayer = sessionService.getUser(authToken).getUsername();
        if (!expectedPlayer.equals(actualPlayer)) {
            throw new RuntimeException("It is not your turn");
        }

        game.getGame().makeMove(move);
        sendGame(game); // send the updated game to all players

        var notification = new Notification(move.toString());
        connections.broadcast(authToken, notification);

    }

    private Connection getConnection(String authToken, Session session) {
        var conn = connections.get(authToken);
        if (conn == null) {
            conn = connections.add(authToken, new Connection(authToken, session));
        }
        return conn;
    }


    private void resign(Connection connection, String message) throws IOException {
        var resignCommand = new Gson().fromJson(message, ResignCommand.class);
        var username = resignCommand.username();
        var authToken = resignCommand.getAuthString();
        connection.game.resign(username);
        var msg = String.format("%s resigned", username);
        var notification = new Notification(msg);
        connections.broadcast(authToken, notification);
    }

    private void leave(Connection connection, String message) throws IOException {
        var leaveGameCommand = new Gson().fromJson(message, LeaveGameCommand.class);
        var username = leaveGameCommand.username();
        var authToken = leaveGameCommand.getAuthString();
        connection.session.close();
        connections.remove(authToken);
        var msg = String.format("%s left the game", username);
        var notification = new Notification(msg);
        connections.broadcast(authToken, notification);
    }

    /**
     * Adds a player to the game, if no color is specified, the player is an observer.
     * A notification is sent to all players in the game.
     */
    private void join(Connection connection, String message) throws Exception {
        var joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);
        var gameID = joinPlayerCommand.gameID();
        var playerColor = joinPlayerCommand.playerColor();
        var authToken = joinPlayerCommand.getAuthString();

        // verify game exists
        var game = gameService.loadGame(gameID);
        if (game == null) {
            throw new Exception("Game not found");
        }

        // verify user is a player in the game
        var username = sessionService.getUser(authToken).getUsername();
        if (playerColor != null && (playerColor == ChessGame.TeamColor.WHITE && !game.getWhiteUsername().equals(username) || playerColor == ChessGame.TeamColor.BLACK && !game.getBlackUsername().equals(username))) {
            throw new Exception("You are not a player in this game");
        }

        connection.game = game; // save the game to the connection
        connections.add(authToken, connection);
        var msg = String.format("%s joined game %d as %s", username, gameID, (playerColor == null ? "observer" : playerColor));
        var notification = new Notification(msg);
        connections.broadcast(authToken, notification);
        sendGame(game, authToken);
    }

    /**
     * Sends the game to the player with the specified authToken.
     */
    private void sendGame(models.Game game, String authToken) throws IOException {
        var message = new Gson().toJson(game);
        var notification = new LoadGameMessage(message);
        connections.send(authToken, notification);
    }

    /**
     * Sends the game to all players in the game.
     */
    private void sendGame(models.Game game) throws IOException {
        var message = new Gson().toJson(game);
        var notification = new LoadGameMessage(message);
        connections.broadcast(null, notification);
    }
}