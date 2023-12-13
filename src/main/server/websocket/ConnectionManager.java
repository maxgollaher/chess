package server.websocket;

import com.google.gson.Gson;
import models.ModelSerializer;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public Connection add(String authToken, Connection connection) {
        connections.put(authToken, connection);
        return connection;
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void broadcast(String excludeAuthToken, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken)) {
                    c.send(new Gson().toJson(notification));
                }
                if (notification instanceof LoadGameMessage) {
                    c.game = ModelSerializer.deserialize(((LoadGameMessage) notification).game(), models.Game.class);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }

    public void send(String authToken, ServerMessage message) throws IOException {
        var connection = connections.get(authToken);
        if (connection.session.isOpen()) {
            connection.send(new Gson().toJson(message));
        }

    }

    public Connection get(String authToken) {
        return connections.get(authToken);
    }
}
