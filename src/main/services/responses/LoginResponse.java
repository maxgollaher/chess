package services.responses;

/**
 * Response object for the {@link services.SessionService#login(services.requests.LoginRequest)} method.
 *
 * @param username  The username of the user that logged in.
 * @param authToken The {@link models.AuthToken} of the user that logged in.
 */
public record LoginResponse(String username, String authToken) {
}
