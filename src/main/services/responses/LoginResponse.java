package services.responses;

import models.AuthToken;
import services.SessionService;
import services.requests.LoginRequest;

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
    private String authToken;

    /**
     * Creates a new LoginResponse object.
     *
     * @param username  the username of the user that logged in.
     * @param authToken the {@link AuthToken} of the user that logged in.
     */
    public LoginResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
