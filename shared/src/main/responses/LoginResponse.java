package responses;

import requests.LoginRequest;

/**
 * Response object for the Login method.
 *
 * @param username  The username of the user that logged in.
 * @param authToken The {@link models.AuthToken} of the user that logged in.
 */
public record LoginResponse(String username, String authToken) {
}