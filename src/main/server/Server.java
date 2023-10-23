package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import handlers.*;
import spark.Request;
import spark.Response;

import static spark.Spark.*;
import java.util.*;

public class Server {

    /**
     * The handlers for the server.
     */
    private static final UserHandler userHandler = new UserHandler();
    private static final SessionHandler sessionHandler = new SessionHandler();
    private static final GameHandler gameHandler = new GameHandler();
    private static final AdminHandler adminHandler = new AdminHandler();


    /**
     * Starts the server.
     *
     * @param args the command line arguments, args[0] is the port number.
     */
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            new Server().run(port);

        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        }
    }

    /**
     * Starts the server on the specified port.
     *
     * @param port the port number to listen on.
     */
    public void run(int port) {
        // Specify Port to listen on
        port(port);
        System.out.println("Listening on port " + port);

        // Serve static files from /chess/web
        externalStaticFileLocation("web");

        // Register handlers for each endpoint using the method reference syntax
        createRoutes();
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
        get("/error", this::throwError);
        exception(DataAccessException.class, this::errorHandler);
        notFound((req, res) -> {
            var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return errorHandler(new DataAccessException(msg), req, res);
        });
    }

    /**
     * Throws an exception.
     *
     * @param request the request object.
     * @param response the response object.
     * @return the response body.
     * @throws DataAccessException always.
     */
    private Object throwError(Request request, Response response) throws DataAccessException {
        throw new DataAccessException("Server is on fire!");
    }

    /**
     * Handles an exception.
     *
     * @param e the exception.
     * @param request the request object.
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
     * @param request the request object.
     * @param response the response object.
     * @return the response body.
     * @throws DataAccessException if there is an error accessing the database.
     */
    private Object clear(Request request, Response response) throws DataAccessException {
        adminHandler.clearDatabase();
        response.type("application/json");
        return new Gson().toJson(Map.of("message", "Clear succeeded"));
    }

    /**
     * Joins a game.
     *
     * @param request the request object.
     * @param response the response object.
     * @return the response body.
     */
    private Object joinGame(Request request, Response response) {
        var bodyObj = getBody(request, Map.class);
        response.type("application/json");
        return new Gson().toJson(bodyObj);
    }

    /**
     * Creates a game.
     *
     * @param request the request object.
     * @param response the response object.
     * @return the response body.
     */
    private Object createGame(Request request, Response response) throws DataAccessException {
        var headerObj = getHeader(request);
        var bodyObj = getBody(request, Map.class);
        response.type("application/json");
        sessionHandler.authorizeUser(headerObj, response);
        gameHandler.createGame(bodyObj, response);
        return response.body();
    }

    /**
     * Lists all games.
     *
     * @param request the request object.
     * @param response the response object.
     * @return the response body.
     */
    private Object listGames(Request request, Response response) {
        var bodyObj = getBody(request, Map.class);
        response.type("application/json");
        return new Gson().toJson(bodyObj);
    }

    /**
     * Logs out.
     *
     * @param request the request object.
     * @param response the response object.
     * @return the response body.
     */
    private Object logout(Request request, Response response) throws DataAccessException {
        var headerObj = getHeader(request);
        response.type("application/json");
        sessionHandler.authorizeUser(headerObj, response);
        sessionHandler.logout(headerObj, response);
        return response.body();
    }



    /**
     * Logs in.
     *
     * @param request the request object.
     * @param response the response object.
     * @return the response body.
     */
    private Object login(Request request, Response response) throws DataAccessException {
        var bodyObj = getBody(request, Map.class);
        // get the class of body object and print it out
        System.out.println(bodyObj.getClass());
        response.type("application/json");
        sessionHandler.login(bodyObj, response);
        return response.body();
    }

    /**
     * Registers a user.
     *
     * @param request the request object.
     * @param response the response object.
     * @return the response body of the register request.
     */
    private Object register(Request request, Response response) throws DataAccessException {
        var bodyObj = getBody(request, Map.class);
        response.type("application/json");
        userHandler.register(bodyObj, response);
        return response.body();
    }

    /**
     * Gets the body of the request.
     *
     * @param request the request object.
     * @param clazz the class of the body.
     * @param <T> the type of the body.
     * @return the body of the request.
     */
    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    private static String getHeader(Request request) {
        var header = request.headers("authorization");
        if (header == null) {
            throw new RuntimeException("missing required header");
        }
        return header;
    }
}
