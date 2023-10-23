package handlers;

import dataAccess.DataAccessException;
import services.AdminService;


public class AdminHandler {

    private static final AdminService adminService = new AdminService();

    public void clearDatabase() throws DataAccessException {
        adminService.clearDatabase();
    }
}
