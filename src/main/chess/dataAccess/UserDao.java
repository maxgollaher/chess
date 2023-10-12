package chess.dataAccess;

public class UserDao {
    private static UserDao instance;

    private UserDao() {}

    public static UserDao getInstance() {
        return instance;
    }

}
