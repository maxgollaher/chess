package requests;


/**
 * Immutable request object for the CreateGame method.
 *
 * @param gameName The name of the game that is being created.
 */
public record CreateGameRequest(String gameName) {
}