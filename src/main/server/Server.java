package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import handlers.AdminHandler;
import handlers.GameHandler;
import handlers.SessionHandler;
import handlers.UserHandler;
import server.websocket.WebSocketHandler;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Map;

import static spark.Spark.*;

public class Server {

    /**
     * The handlers for the server.
     */
    private static final UserHandler userHandler = new UserHandler();
    private static final SessionHandler sessionHandler = new SessionHandler();
    private static final GameHandler gameHandler = new GameHandler();
    private static final AdminHandler adminHandler = new AdminHandler();

    private static final WebSocketHandler webSocketHandler = new WebSocketHandler();


    /**
     * Starts the server.
     *
     * @param args the command line arguments, args[0] is the port number.
     */
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            new Server().run(port);

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        }
    }

    /**
     * Gets the body of the request.
     *
     * @param request the request object.
     * @return the body of the request.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getBody(Request request) {
        var body = new Gson().fromJson(request.body(), Map.class);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    /**
     * Gets the header of the request.
     *
     * @param request the request object.
     * @return the header of the request.
     */
    private static String getHeader(Request request) {
        var header = request.headers("authorization");
        if (header == null) {
            throw new RuntimeException("missing required header");
        }
        return header;
    }

    /**
     * Starts the server on the specified port.
     *
     * @param port the port number to listen on.
     */
    public void run(int port) {
        port(port);
        System.out.println("Listening on port " + port);
        externalStaticFileLocation("web");
        webSocket("/connect", webSocketHandler);
        createRoutes();
        awaitInitialization();
    }

    /**
     * Creates the routes for the server.
     */
    private void createRoutes() {
        // creating endpoints
        post("/user", this::register);
        post("/session", this::login);
        delete("/session", this::logout);
        get("/game", this::listGames);
        post("/game", this::createGame);
        put("/game", this::joinGame);
        delete("/db", this::clear);

        // error handling
        exception(DataAccessException.class, this::errorHandler);
        notFound((req, res) -> {
            var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return errorHandler(new DataAccessException(msg), req, res);
        });
    }

    /**
     * Handles an exception.
     *
     * @param e        the exception.
     * @param request  the request object.
     * @param response the response object.
     * @return the response body.
     */
    private Object errorHandler(DataAccessException e, Request request, Response response) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        response.type("application/json");
        if (response.status() == 200) {
            response.status(500);
        }
        response.body(body);
        return body;
    }

    /**
     * Clears the database.
     *
     * @return the response body.
     * @throws DataAccessException if there is an error accessing the database.
     */
    private Object clear(Request request, Response response) throws DataAccessException {
        adminHandler.clearDatabase(response);
        return response.body();
    }

    /**
     * Joins a game after the user is authorized.
     *
     * @param request  the request object.
     * @param response the response object.
     * @return the response body.
     */
    private Object joinGame(Request request, Response response) throws DataAccessException, IOException {
        var authToken = getHeader(request);
        var bodyObj = getBody(request);
        response.type("application/json");
        sessionHandler.authorizeUser(authToken, response);
        gameHandler.joinGame(bodyObj, authToken, response); // pass authToken in to later get the username
        return response.body();
    }

    /**
     * Creates a game after the user is authorized.
     *
     * @param request  the request object.
     * @param response the response object.
     * @return the response body.
     */
    private Object createGame(Request request, Response response) throws DataAccessException {
        var authToken = getHeader(request);
        var bodyObj = getBody(request);
        response.type("application/json");
        sessionHandler.authorizeUser(authToken, response);
        gameHandler.createGame(bodyObj, response);
        return response.body();
    }

    /**
     * Lists all games after the user is authorized.
     *
     * @param request  the request object.
     * @param response the response object.
     * @return the response body.
     */
    private Object listGames(Request request, Response response) throws DataAccessException {
        var authToken = getHeader(request);
        response.type("application/json");
        sessionHandler.authorizeUser(authToken, response);
        gameHandler.listGames(response);
        return response.body();
    }

    /**
     * Logs out after the user is authorized.
     *
     * @param request  the request object.
     * @param response the response object.
     * @return the response body.
     */
    private Object logout(Request request, Response response) throws DataAccessException {
        var authToken = getHeader(request);
        response.type("application/json");
        sessionHandler.authorizeUser(authToken, response);
        sessionHandler.logout(authToken, response);
        return response.body();
    }

    /**
     * Logs in.
     *
     * @param request  the request object.
     * @param response the response object.
     * @return the response body containing the auth token if the login was successful.
     */
    private Object login(Request request, Response response) throws DataAccessException {
        var bodyObj = getBody(request);
        response.type("application/json");
        sessionHandler.login(bodyObj, response);
        return response.body();
    }

    /**
     * Registers a user.
     *
     * @param request  the request object.
     * @param response the response object.
     * @return the response body of the register request.
     */
    private Object register(Request request, Response response) throws DataAccessException {
        var bodyObj = getBody(request);
        response.type("application/json");
        userHandler.register(bodyObj, response);
        return response.body();
    }
}
