package chess.dataAccess;

import chess.models.Game;

public class GameDao {
    private static GameDao instance;

    private GameDao() {}

    public static GameDao getInstance() {
        return instance;
    }
}
