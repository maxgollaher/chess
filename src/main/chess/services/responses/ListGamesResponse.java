package chess.services.responses;

import chess.models.Game;

import java.util.List;

public class ListGamesResponse {
    private List<Game> games;

    public ListGamesResponse(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
