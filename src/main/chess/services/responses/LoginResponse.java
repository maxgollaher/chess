package chess.services.responses;

import chess.models.AuthToken;
import chess.services.SessionService;
import chess.services.requests.LoginRequest;

/**
 * Response object for the {@link SessionService#login(LoginRequest)} method.
 */
public class LoginResponse {

    /**
     * The username of the user that logged in.
     */
    private String username;

    /**
     * The {@link AuthToken} of the user that logged in.
     */
    private AuthToken authToken;

    /**
     * Creates a new LoginResponse object.
     *
     * @param username  the username of the user that logged in.
     * @param authToken the {@link AuthToken} of the user that logged in.
     */
    public LoginResponse(String username, AuthToken authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

}
