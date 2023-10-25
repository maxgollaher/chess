package services.responses;

import models.AuthToken;
import services.SessionService;
import services.requests.LoginRequest;

/**
 * Response object for the {@link SessionService#login(LoginRequest)} method.
 *
 * @param username  The username of the user that logged in.
 * @param authToken The {@link AuthToken} of the user that logged in.
 */
public record LoginResponse(String username, String authToken) {
}
