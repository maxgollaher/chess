package services;

import dataAccess.AuthTokenDao;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import models.AuthToken;
import models.Game;
import services.requests.CreateGameRequest;
import services.requests.JoinGameRequest;
import services.responses.CreateGameResponse;
import services.responses.ListGamesResponse;

import java.util.ArrayList;

/**
 * The Game Service class handles all requests to the /game endpoint of the API.
 * It interacts with the {@link GameDao} to create games, join games, and list games.
 */
public class GameService {

    /**
     * The {@link GameDao} to be used to access the game database
     */
    private final GameDao gameDao = GameDao.getInstance();

    /**
     * The {@link AuthTokenDao} to be used to access the {@link AuthToken} database
     */
    private final AuthTokenDao authTokenDao = AuthTokenDao.getInstance();


    /**
     * Joins a player to a game based on a given JoinGameRequest
     *
     * @param request a {@link JoinGameRequest} to join a game, which contains the player's
     *                {@link AuthToken}, player color, and the game id.
     * @throws DataAccessException if there is an error accessing the database or if the game
     *                             position is already taken.
     */
    public void joinGame(JoinGameRequest request) throws DataAccessException {
        var game = gameDao.find(request.getGameID());
        if (game == null) {
            throw new DataAccessException("game not found");
        }
        var username = authTokenDao.find(request.getAuthToken()).getUsername(); // the user has already been authorized, so this will always work
        gameDao.claimSpot(username, request.getPlayerColor(), request.getGameID());
    }

    /**
     * Creates a new game based on a given CreateGameRequest
     *
     * @param request a {@link CreateGameRequest} to create a new game, which contains the
     *                player's {@link AuthToken} and the game name.
     * @return a {@link CreateGameResponse} containing the game id of the created game.
     * @throws DataAccessException if there is an error accessing the database or the user lacks authorization.
     */
    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        var game = new Game(request.getGameName());
        gameDao.insert(game);
        return new CreateGameResponse(game.getGameID());
    }

    /**
     * Lists all games that are currently in progress.
     *
     * @return a {@link ListGamesResponse} containing a list of all games that are currently in progress.
     * @throws DataAccessException if there is an error accessing the database or the user lacks authorization.
     */
    public ListGamesResponse listGames() throws DataAccessException {
        ArrayList<Game> gameList = new ArrayList<>(gameDao.findAll());
        return new ListGamesResponse(gameList);
    }

}
