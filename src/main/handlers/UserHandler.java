package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import services.UserService;
import services.requests.RegisterRequest;
import services.responses.LoginResponse;
import spark.Response;

import java.util.Map;

public class UserHandler {

    private static final UserService userService = new UserService();

    public void register(Map<String, Object> bodyObj, Response response) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(bodyObj.toString(), RegisterRequest.class);
        if (registerRequest.getUsername() == null || registerRequest.getPassword() == null || registerRequest.getEmail() == null) {
            response.status(400);
            throw new DataAccessException("bad request");
        }
        try {
            LoginResponse loginResponse = userService.register(registerRequest);
            response.status(200);
            response.body(new Gson().toJson(loginResponse));

        } catch (DataAccessException e) {
            response.status(403);
            throw e;
        }
    }
}
