package responses;

import java.util.ArrayList;

/**
 * Response object for the ListGames method.
 *
 * @param games The {@link ArrayList} of games that are currently in the database.
 */
public record ListGamesResponse(ArrayList<models.Game> games) {
}