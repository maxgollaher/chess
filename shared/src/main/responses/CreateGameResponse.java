package responses;


import requests.CreateGameRequest;

/**
 * Response object for the CreateGame method.
 *
 * @param gameID The id of the game that was created.
 */
public record CreateGameResponse(int gameID) {
}