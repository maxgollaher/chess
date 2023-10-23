package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import services.SessionService;
import services.requests.LoginRequest;
import services.responses.LoginResponse;
import spark.Response;

import java.util.Map;


public class SessionHandler {

    private static final SessionService sessionService = new SessionService();

    public void login(Map bodyObj, Response response) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(bodyObj.toString(), LoginRequest.class);
        try {
            LoginResponse loginResponse = sessionService.login(loginRequest);
            response.header("Authorization", loginResponse.getAuthToken());
            response.body(new Gson().toJson(loginResponse));
        } catch (DataAccessException e) {
            response.status(401);
            throw e;
        }
    }

    /**
     * Logs out the user. At this point, the user is already authorized.
     *
     * @param authToken the auth token of the user.
     * @param response the response object.
     * @return the response body.
     * @throws DataAccessException if there is an error accessing the database.
     */
    public void logout(String authToken, Response response) throws DataAccessException {
            sessionService.logout(authToken);
            response.body(new JsonObject().toString());
    }

    /**
     * Authorizes the user based on an AuthToken. If the user is not authorized, the response status is set to 401.
     * @param headerObj
     * @param response
     * @throws DataAccessException
     */
    public void authorizeUser(String headerObj, Response response) throws DataAccessException {
        try {
            sessionService.authorizeUser(headerObj);
        } catch (DataAccessException e) {
            response.status(401);
            throw e;
        }
    }
}
