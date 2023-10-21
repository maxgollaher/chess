package handlers;

import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import services.AdminService;

import java.net.http.HttpResponse;

public class AdminHandler {

    private static final AdminService adminService = new AdminService();

    public void clearDatabase() throws DataAccessException {
        adminService.clearDatabase();
    }
}
