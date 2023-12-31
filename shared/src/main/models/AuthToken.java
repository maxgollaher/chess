package models;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an authentication token. Contains the token itself and the username of the user it belongs to.
 */
public class AuthToken {

    /**
     * The authentication token.
     */
    private String authToken;

    /**
     * The username of the user that the token belongs to.
     */
    private String username;

    /**
     * Blank constructor for AuthToken, used to create null AuthToken objects if needed.
     */
    public AuthToken() {
    }

    /**
     * Creates a new AuthToken object.
     *
     * @param username the username of the user that the token belongs to.
     */
    public AuthToken(String username) {
        this.authToken = generateNewToken();
        this.username = username;
    }

    /**
     * Creates a new AuthToken object.
     *
     * @param authToken the authentication token.
     * @param username  the username of the user that the token belongs to.
     */
    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    /**
     * Generates a new random authentication token.
     *
     * @return a new authentication token.
     */
    public static String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return Objects.equals(authToken, authToken1.authToken) && Objects.equals(username, authToken1.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "authToken='" + authToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
