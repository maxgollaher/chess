package models;

import chess.ChessGame;
import chess.ChessPiece;
import chess.Game;
import com.google.gson.*;
import responses.ListGamesResponse;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

public class ModelSerializer {

    public static <T> T deserialize(String json, Class<T> responseClass) {
        return deserialize(new StringReader(json), responseClass);
    }

    public static <T> T deserialize(Reader reader, Class<T> responseClass) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(chess.Game.class, new ChessGameAdapter());
        gsonBuilder.registerTypeAdapter(chess.ChessGame.class, new ChessGameAdapter());
        gsonBuilder.registerTypeAdapter(chess.Board.class, new ChessBoardAdapter());
        gsonBuilder.registerTypeAdapter(chess.Piece.class, new ChessPieceAdapter());
        gsonBuilder.registerTypeAdapter(ListGamesResponse.class, new ListGamesResponseAdapter());
        return gsonBuilder.create().fromJson(reader, responseClass);
    }

    /**
     * Deserializes a game from the database
     *
     * @param resultSet the result set to deserialize
     * @return the deserialized game
     */
    public static chess.Game jsonToGame(ResultSet resultSet) throws SQLException {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(chess.Game.class, new ChessGameAdapter());
        builder.registerTypeAdapter(chess.Board.class, new ChessBoardAdapter());
        builder.registerTypeAdapter(chess.Piece.class, new ChessPieceAdapter());
        var gson = builder.create();
        return gson.fromJson(resultSet.getString("game"), chess.Game.class);
    }

    public static class ListGamesResponseAdapter implements JsonDeserializer<ListGamesResponse> {

        @Override
        public ListGamesResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var response = jsonElement.getAsJsonObject();
            var responseGames = response.get("games");
            ArrayList<models.Game> games = new ArrayList<>();
            for (JsonElement game : responseGames.getAsJsonArray()) {
                var deserializedGame = jsonDeserializationContext.deserialize(game, models.Game.class);
                games.add((models.Game) deserializedGame);
            }
            return new ListGamesResponse(games);
        }
    }

    /**
     * Deserializer for the {@link chess.Game} class
     */
    public static class ChessGameAdapter implements JsonDeserializer<Game> {

        @Override
        public chess.Game deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var jsonObject = jsonElement.getAsJsonObject();

            // give context to each element as necessary, boards are handled by ChessBoardAdapter, pieces are handled by ChessPieceAdapter
            chess.Board board = jsonDeserializationContext.deserialize(jsonObject.get("board"), chess.Board.class);
            ChessGame.TeamColor teamTurn = ChessGame.TeamColor.valueOf(jsonObject.get("teamTurn").getAsString());
            chess.Piece isEnPassant = jsonDeserializationContext.deserialize(jsonObject.get("isEnPassant"), chess.Piece.class);
            Set<ChessPiece> hasMoved = jsonDeserializationContext.deserialize(jsonObject.get("hasMoved"), Set.class);
            return new chess.Game(board, teamTurn, isEnPassant, hasMoved);
        }
    }

    /**
     * Deserializer for the {@link chess.Board} class
     */
    public static class ChessBoardAdapter implements JsonDeserializer<chess.Board> {

        @Override
        public chess.Board deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var jsonObject = jsonElement.getAsJsonObject();

            // give context to the pieces in the array
            chess.Piece[][] board = jsonDeserializationContext.deserialize(jsonObject.get("board"), chess.Piece[][].class);
            return new chess.Board(board);
        }
    }

    /**
     * Deserializer for the {@link chess.Piece} class
     */
    public static class ChessPieceAdapter implements JsonDeserializer<chess.Piece> {

        @Override
        public chess.Piece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var jsonObject = jsonElement.getAsJsonObject();

            // extract all elements, return a new piece with the same elements
            ChessGame.TeamColor teamColor = ChessGame.TeamColor.valueOf(jsonObject.get("teamColor").getAsString());
            ChessPiece.PieceType pieceType = ChessPiece.PieceType.valueOf(jsonObject.get("pieceType").getAsString());
            int id = jsonObject.get("id").getAsInt();
            return new chess.Piece(teamColor, pieceType, id);
        }
    }

}
