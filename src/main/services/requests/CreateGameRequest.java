package services.requests;


/**
 * Immutable request object for the {@link services.GameService#createGame(CreateGameRequest)} method.
 *
 * @param gameName The name of the game that is being created.
 */
public record CreateGameRequest(String gameName) {
}
