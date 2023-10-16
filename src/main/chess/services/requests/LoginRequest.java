package chess.services.requests;

import chess.services.SessionService;

/**
 * Request object for the {@link SessionService#login(LoginRequest)} method.
 */
public class LoginRequest {

    /**
     * The username of the user that is logging in.
     */
    private String username;

    /**
     * The password of the user that is logging in.
     */
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
}
