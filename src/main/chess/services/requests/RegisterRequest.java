package chess.services.requests;

import chess.services.UserService;

/**
 * Request object for the {@link UserService#register(RegisterRequest)} method.
 */
public class RegisterRequest {

    /**
     * The username of the user that is registering.
     */
    private String username;

    /**
     * The password of the user that is registering.
     */
    private String password;

    /**
     * The email of the user that is registering.
     */
    private String email;

    /**
     * Creates a new RegisterRequest object.
     *
     * @param username the username of the user that is registering.
     * @param password the password of the user that is registering.
     * @param email    the email of the user that is registering.
     */
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
