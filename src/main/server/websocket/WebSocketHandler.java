package server.websocket;


import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import requests.JoinGameRequest;
import services.GameService;
import webSocketMessages.*;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Map;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private static final GameService gameService = new GameService();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER:
                joinGame(new Gson().fromJson(message, JoinPlayerCommand.class), session);
                break;
            case LEAVE:
                leaveGame(new Gson().fromJson(message, LeaveGameCommand.class));
                break;
            case MAKE_MOVE:
                break;
            case RESIGN:
                resignGame(new Gson().fromJson(message, ResignCommand.class));
                break;
        }
    }

    private void resignGame(ResignCommand resignCommand) throws IOException {
        var username = resignCommand.username();
        var authToken = resignCommand.getAuthString();
        var message = String.format("%s resigned", username);
        var notification = new Notification(message);
        connections.broadcast(authToken, notification);
    }

    private void leaveGame(LeaveGameCommand leaveGameCommand) throws IOException {
        var username = leaveGameCommand.username();
        var authToken = leaveGameCommand.getAuthString();
        connections.remove(authToken);
        var message = String.format("%s left the game", username);
        var notification = new Notification(message);
        connections.broadcast(authToken, notification);
    }

    /**
     * Adds a player to the game, if no color is specified, the player is an observer.
     * A notification is sent to all players in the game.
     */
    private void joinGame(JoinPlayerCommand joinPlayerCommand, Session session) throws IOException {
        var username = joinPlayerCommand.username();
        var gameID = joinPlayerCommand.gameID();
        var playerColor = joinPlayerCommand.playerColor();
        var authToken = joinPlayerCommand.getAuthString();
        connections.add(authToken, session);
        var message = String.format("%s joined game %d as %s", username, gameID, (playerColor == null ? "observer" : playerColor));
        var notification = new Notification(message);
        connections.broadcast(authToken, notification);
    }

    /**
     * Retrieves the game from the database and sends it to the client.
     */
    public void sendGame(Map<String, Object> bodyObj, String authToken) throws DataAccessException, IOException {
        bodyObj.put("authToken", authToken);
        JoinGameRequest joinGameRequest = new Gson().fromJson(new Gson().toJson(bodyObj), JoinGameRequest.class);
        var gameID = joinGameRequest.gameID();
        var message = new Gson().toJson(gameService.loadGame(gameID));
        var notification = new LoadGameMessage(message);
        connections.send(authToken, notification);
    }
}