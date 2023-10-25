package services.responses;

import services.GameService;
import services.requests.CreateGameRequest;

/**
 * Response object for the {@link GameService#createGame(CreateGameRequest)} method.
 *
 * @param gameID The id of the game that was created.
 */
public record CreateGameResponse(int gameID) {
}
