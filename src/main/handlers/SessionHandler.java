package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import org.eclipse.jetty.security.LoginService;
import services.SessionService;
import services.requests.LoginRequest;
import services.responses.LoginResponse;
import spark.Response;

import java.util.Map;

public class SessionHandler {

    private static final SessionService sessionService = new SessionService();

    public void login(Map<String, Object> bodyObj, Response response) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(bodyObj.toString(), LoginRequest.class);
        try {
            LoginResponse loginResponse = sessionService.login(loginRequest);
            response.status(200);
            response.header("Authorization", loginResponse.getAuthToken());
            response.body(new Gson().toJson(loginResponse));
        } catch (DataAccessException e) {
            response.status(401);
            throw e;
        }
    }

    public void logout(String authToken, Response response) throws DataAccessException {
        try {
            sessionService.logout(authToken);
            response.status(200);
            // create an empty response body json object
            response.body(new JsonObject().toString());
        } catch (DataAccessException e) {
            response.status(401);
            throw e;
        }

    }
}
