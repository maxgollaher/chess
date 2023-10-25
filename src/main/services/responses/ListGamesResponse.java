package services.responses;

import java.util.ArrayList;

/**
 * Response object for the {@link services.GameService#listGames()} method.
 *
 * @param games The {@link ArrayList} of games that are currently in the database.
 */
public record ListGamesResponse(ArrayList<models.Game> games) {
}
