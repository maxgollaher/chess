package chess.dataAccess;

public class AuthTokenDao {
    private static AuthTokenDao instance;

    private AuthTokenDao() {}

    public static AuthTokenDao getInstance() {
        return instance;
    }
}
