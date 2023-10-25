package services.responses;


/**
 * Response object for the {@link services.GameService#createGame(services.requests.CreateGameRequest)} method.
 *
 * @param gameID The id of the game that was created.
 */
public record CreateGameResponse(int gameID) {
}
