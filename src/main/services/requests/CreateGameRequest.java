package services.requests;

import services.GameService;

/**
 * Immutable request object for the {@link GameService#createGame(CreateGameRequest)} method.
 *
 * @param gameName The name of the game that is being created.
 */
public record CreateGameRequest(String gameName) {
}
