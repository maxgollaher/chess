package daoTests;

import dataAccess.AuthTokenDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

public class AuthTokenDaoTest {

    private static final AuthTokenDao authTokenDao;

    static {
        try {
            authTokenDao = new AuthTokenDao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        // Make sure that the database is clear before each test
        Assertions.assertDoesNotThrow(authTokenDao::clear);
        Assertions.assertTrue(Assertions.assertDoesNotThrow(authTokenDao::findAll).isEmpty());
    }




}
