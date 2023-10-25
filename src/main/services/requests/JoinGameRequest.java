package services.requests;


/**
 * Immutable request object for the {@link services.GameService#joinGame(JoinGameRequest)} method.
 *
 * @param playerColor The {@link chess.ChessGame.TeamColor} of the player that is joining the game. Can be left empty to
 *                    allow for watching the game.
 * @param gameID      The id of the game that the player is joining.
 * @param authToken   The auth token of the player joining the game.
 */
public record JoinGameRequest(chess.ChessGame.TeamColor playerColor, int gameID, String authToken) {
}