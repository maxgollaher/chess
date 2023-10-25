package services.requests;

import services.SessionService;

/**
 * Immutable request object for the {@link SessionService#login(LoginRequest)} method.
 *
 * @param username The username of the user that is logging in.
 * @param password The password of the user that is logging in.
 */
public record LoginRequest(String username, String password) {
}
