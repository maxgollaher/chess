package chess.services.responses;

import chess.models.Game;

public class ListGamesResponse {
    private Game[] games;

    public ListGamesResponse(Game[] games) {
        this.games = games;
    }

    public Game[] getGames() {
        return games;
    }

    public void setGames(Game[] games) {
        this.games = games;
    }
}
