package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import services.UserService;
import services.requests.RegisterRequest;
import services.responses.LoginResponse;
import spark.Response;

import java.util.Map;

/**
 * Handler for the /user endpoint
 */
public class UserHandler {

    private static final UserService userService = new UserService();

    /**
     * Registers a new user based on a given RegisterRequest
     *
     * @param bodyObj  the body of the request, containing the username, password, and email
     * @param response the spark response object to be modified with the data from the LoginResponse
     * @throws DataAccessException when the request is invalid/missing data, and when the Service throws an exception
     *                             if the username/password is already taken
     */
    public void register(Map<String, Object> bodyObj, Response response) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(bodyObj.toString(), RegisterRequest.class);
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            response.status(400);
            throw new DataAccessException("bad request");
        }
        try {
            LoginResponse loginResponse = userService.register(registerRequest);
            response.body(new Gson().toJson(loginResponse));

        } catch (DataAccessException e) {
            // username/password already taken
            response.status(403);
            throw e;
        }
    }
}
