package server.websocket;

import com.google.gson.Gson;
import models.Game;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.ErrorMessage;

import java.io.IOException;

public class Connection {
    public String authToken;
    public Session session;
    public Game game;

    public Connection(String authToken, Session session) {
        this.authToken = authToken;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public static void sendError(RemoteEndpoint endpoint, String s) throws IOException {
        endpoint.sendString(new Gson().toJson(new ErrorMessage(s)));
    }
}