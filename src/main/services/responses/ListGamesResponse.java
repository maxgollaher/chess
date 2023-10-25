package services.responses;

import models.Game;
import services.GameService;

import java.util.ArrayList;

/**
 * Response object for the {@link GameService#listGames()} method.
 *
 * @param games The list of games that are currently in the database.
 */
public record ListGamesResponse(ArrayList<Game> games) {
}
