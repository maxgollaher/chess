package services.requests;


/**
 * Immutable request object for the {@link services.UserService#register(RegisterRequest)} method.
 *
 * @param username The username of the user that is registering.
 * @param password The password of the user that is registering.
 * @param email    The email of the user that is registering.
 */
public record RegisterRequest(String username, String password, String email) {
}