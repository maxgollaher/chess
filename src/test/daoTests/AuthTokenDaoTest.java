package daoTests;

import dataAccess.AuthTokenDao;
import dataAccess.DataAccessException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Assertions.assertDoesNotThrow(authTokenDao::clear);
    }

    @Test
    @DisplayName("Test insert authToken")
    void testInsert() {
        // test inserting a new authToken
        AuthToken testAuthToken = new AuthToken("testAuth", "username");
        Assertions.assertDoesNotThrow(() -> authTokenDao.insert(testAuthToken));

        // see if the authToken was inserted
        var foundToken = Assertions.assertDoesNotThrow(() -> authTokenDao.find("testAuth"));
        Assertions.assertEquals(testAuthToken, foundToken);
    }

    @Test
    @DisplayName("Test insert authToken fail")
    void testInsertFail() {
        // test insert
        AuthToken testAuthToken = new AuthToken("testAuth", "username");
        Assertions.assertDoesNotThrow(() -> authTokenDao.insert(testAuthToken));

        // test inserting a duplicate authToken with the same primary key
        Assertions.assertThrows(DataAccessException.class, () -> authTokenDao.insert(testAuthToken));
    }

    @Test
    @DisplayName("Test delete authToken")
    void testDelete() {
        // test deleting an authToken
        AuthToken testAuthToken = new AuthToken("testAuth", "username");
        Assertions.assertDoesNotThrow(() -> authTokenDao.insert(testAuthToken));
        Assertions.assertDoesNotThrow(() -> authTokenDao.delete(testAuthToken.getAuthToken()));

        // see if the authToken was deleted
        Assertions.assertNull(Assertions.assertDoesNotThrow(() -> authTokenDao.find("testAuth")));
    }

    @Test
    @DisplayName("Test delete authToken fail")
    void testDeleteFail() {
        // SQL doesn't throw an error when a nonexistent authToken is deleted
        // test that deleting a nonexistent authToken doesn't throw an error
        Assertions.assertDoesNotThrow(() -> authTokenDao.delete("authToken"));
    }

    @Test
    @DisplayName("Test find authToken")
    void testFind() {
        // test finding an authToken
        AuthToken testAuthToken = new AuthToken("testAuth", "username");
        Assertions.assertDoesNotThrow(() -> authTokenDao.insert(testAuthToken));

        var foundToken = Assertions.assertDoesNotThrow(() -> authTokenDao.find("testAuth"));
        Assertions.assertEquals(testAuthToken, foundToken);
    }

    @Test
    @DisplayName("Test find authToken fail")
    void testFindFail() {
        // test that finding a nonexistent authToken doesn't throw an error, and that it returns null
        Assertions.assertNull(Assertions.assertDoesNotThrow(() -> authTokenDao.find("nonexistent")));
    }

    @Test
    @DisplayName("Test find all authTokens")
    void testFindAll() {
        // insert several tokens
        AuthToken testAuthToken = new AuthToken("testAuth", "username");
        AuthToken testAuthToken2 = new AuthToken("testAuth2", "username2");
        Assertions.assertDoesNotThrow(() -> authTokenDao.insert(testAuthToken));
        Assertions.assertDoesNotThrow(() -> authTokenDao.insert(testAuthToken2));

        // see if the authTokens were found
        var foundTokens = Assertions.assertDoesNotThrow(authTokenDao::findAll);
        Assertions.assertEquals(2, foundTokens.size());
        Assertions.assertTrue(foundTokens.contains(testAuthToken));
        Assertions.assertTrue(foundTokens.contains(testAuthToken2));
    }

    @Test
    @DisplayName("Test find all authTokens fail")
    void testFindAllFail() {
        // test that the result list is empty when there are no authTokens
        Assertions.assertTrue(Assertions.assertDoesNotThrow(authTokenDao::findAll).isEmpty());
    }

    @Test
    @DisplayName("Test clear authTokens")
    void testClear() {
        // insert a token, assert that it is in the database
        AuthToken testAuthToken = new AuthToken("testAuth", "username");
        Assertions.assertDoesNotThrow(() -> authTokenDao.insert(testAuthToken));
        Assertions.assertFalse(Assertions.assertDoesNotThrow(authTokenDao::findAll).isEmpty());

        // clear the database, assert that the token is no longer in the database
        Assertions.assertDoesNotThrow(authTokenDao::clear);
        Assertions.assertTrue(Assertions.assertDoesNotThrow(authTokenDao::findAll).isEmpty());
    }
}
