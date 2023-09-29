package chess;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

public class Board implements ChessBoard {

    private final ChessPiece[][] board;

    public Board() {
        this.board = new ChessPiece[8][8];
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        var row = position.getRow() - 1;
        var column = position.getColumn() - 1;
        board[row][column] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that position
     */
    @Override
    public ChessPiece getPiece(ChessPosition position) {
        var row = position.getRow() - 1;
        var column = position.getColumn() - 1;
        return board[row][column];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    @Override
    public void resetBoard() {
        board[0] = setRow(WHITE, true);
        board[1] = setRow(WHITE, false);
        board[6] = setRow(BLACK, false);
        board[7] = setRow(BLACK, true);

    }

    private ChessPiece[] setRow(ChessGame.TeamColor color, boolean isBackRow) {
        ChessPiece[] row;
        if (isBackRow) {
            row = new ChessPiece[]{
                    new Piece(color, ROOK),
                    new Piece(color, KNIGHT),
                    new Piece(color, BISHOP),
                    new Piece(color, QUEEN),
                    new Piece(color, KING),
                    new Piece(color, BISHOP),
                    new Piece(color, KNIGHT),
                    new Piece(color, ROOK)
            };
        } else {
            row = new ChessPiece[]{
                    new Piece(color, PAWN),
                    new Piece(color, PAWN),
                    new Piece(color, PAWN),
                    new Piece(color, PAWN),
                    new Piece(color, PAWN),
                    new Piece(color, PAWN),
                    new Piece(color, PAWN),
                    new Piece(color, PAWN),
            };
        }
        return row;
    }
}
