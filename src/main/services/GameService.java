package services;

import dataAccess.AuthTokenDao;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import models.AuthToken;
import models.Game;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;

import java.util.ArrayList;

/**
 * The Game Service class handles all requests to the /game endpoint of the API.
 * It interacts with the {@link GameDao} to create games, join games, and list games.
 * <p>
 * This class does not handle User authentication. It assumes that the user has already
 * been authenticated by the {@link AdminService} class.
 */
public class GameService {

    /**
     * The {@link GameDao} to be used to access the game database
     */
    private static final GameDao gameDao;

    /**
     * The {@link AuthTokenDao} to be used to access the {@link AuthToken} database
     */
    private static final AuthTokenDao authTokenDao;

    static {
        try {
            authTokenDao = new AuthTokenDao();
            gameDao = new GameDao();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Joins a player to a game based on a given JoinGameRequest
     *
     * @param request a {@link JoinGameRequest} to join a game, which contains the player's
     *                {@link AuthToken}, player color, and the game id.
     * @throws DataAccessException if there is an error accessing the database or if the game
     *                             position is already taken.
     */
    public void joinGame(JoinGameRequest request) throws DataAccessException {
        var game = gameDao.find(request.gameID());
        if (game == null) {
            throw new DataAccessException("game not found");
        }
        // the user has already been authorized, so this will always work
        var username = authTokenDao.find(request.authToken()).getUsername();
        gameDao.claimSpot(username, request.playerColor(), request.gameID());
    }

    /**
     * Creates a new game based on a given CreateGameRequest
     *
     * @param request a {@link CreateGameRequest} to create a new game, which contains the
     *                player's {@link AuthToken} and the game name.
     * @return a {@link CreateGameResponse} containing the game id of the created game.
     * @throws DataAccessException if there is an error accessing the database.
     */
    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        var game = new Game(request.gameName());
        gameDao.insert(game);
        return new CreateGameResponse(game.getGameID());
    }

    /**
     * Lists all games that are currently in progress.
     *
     * @return a {@link ListGamesResponse} containing a list of all games that are currently in progress.
     * @throws DataAccessException if there is an error accessing the database.
     */
    public ListGamesResponse listGames() throws DataAccessException {
        ArrayList<Game> gameList = new ArrayList<>(gameDao.findAll());
        return new ListGamesResponse(gameList);
    }

    /**
     * Loads a game from the database
     *
     * @param gameID the id of the game to be loaded
     * @return the {@link Game} with the given id or null if it does not exist
     * @throws DataAccessException if there is an error accessing the database
     */
    public Game loadGame(int gameID) throws DataAccessException {
        return gameDao.find(gameID);
    }
}
