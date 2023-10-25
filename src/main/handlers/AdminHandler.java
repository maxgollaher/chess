package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import services.AdminService;
import spark.Response;

import java.util.Map;

/**
 * Handler for /db endpoints.
 */
public class AdminHandler {

    private static final AdminService adminService = new AdminService();

    /**
     * Clears the database.
     *
     * @param response the response object.
     * @throws DataAccessException if there is an error accessing the database.
     */
    public void clearDatabase(Response response) throws DataAccessException {
        adminService.clearDatabase();
        response.body(new Gson().toJson(Map.of("message", "Clear succeeded", "success", true)));
    }
}
