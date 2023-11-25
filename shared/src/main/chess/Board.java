package chess;

import java.util.Arrays;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

public class Board implements ChessBoard {

    private final ChessPiece[][] board;

    public Board() {
        this.board = new ChessPiece[8][8];
    }

    public Board(ChessPiece[][] board) {
        this.board = board;
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
            row = new ChessPiece[]{new Piece(color, ROOK), new Piece(color, KNIGHT), new Piece(color, BISHOP), new Piece(color, KING), new Piece(color, QUEEN), new Piece(color, BISHOP), new Piece(color, KNIGHT), new Piece(color, ROOK)};
        } else {
            row = new ChessPiece[]{new Piece(color, PAWN), new Piece(color, PAWN), new Piece(color, PAWN), new Piece(color, PAWN), new Piece(color, PAWN), new Piece(color, PAWN), new Piece(color, PAWN), new Piece(color, PAWN),};
        }
        return row;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(ChessPiece[] row : board){
            sb.append(Arrays.toString(row)).append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return Arrays.deepEquals(board, board1.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
