package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import models.ModelSerializer;
import requests.CreateGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.LoginResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        return makeRequest(method, path, null, request, responseClass);
    }

    private <T> T makeRequest(String method, String path, String authToken, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }
            writeBody(request, http);
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "Failure: " + status + " " + http.getResponseMessage());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = ModelSerializer.deserialize(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public LoginResponse register(String[] params) throws ResponseException {
        var path = "/user";
        var request = new RegisterRequest(params[0], params[1], params[2]);
        return makeRequest("POST", path, request, LoginResponse.class);
    }

    public LoginResponse login(String[] params) throws ResponseException {
        var path = "/session";
        var request = new LoginRequest(params[0], params[1]);
        return makeRequest("POST", path, request, LoginResponse.class);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        makeRequest("DELETE", path, authToken, null, null);
    }

    public CreateGameResponse createGame(String[] params, String authToken) throws ResponseException {
        var path = "/game";
        var request = new CreateGameRequest(params[0]);
        return makeRequest("POST", path, authToken, request, CreateGameResponse.class);
    }

    public ListGamesResponse listGames(String authToken) throws ResponseException {
        var path = "/game";
        return makeRequest("GET", path, authToken, null, ListGamesResponse.class);
    }

    public void joinGame(int gameID, ChessGame.TeamColor playerColor, String authToken) throws ResponseException {
        var path = "/game";
        record JoinGameRequest(ChessGame.TeamColor playerColor, int gameID) { };
        var request = new JoinGameRequest(playerColor, gameID);
        makeRequest("PUT", path, authToken, request, null);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        makeRequest("DELETE", path, null, null);
    }
}