package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


enum Direction {
    UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, K_UP_LEFT, K_UP_RIGHT, K_DOWN_LEFT, K_DOWN_RIGHT, K_LEFT_UP, K_LEFT_DOWN, K_RIGHT_UP, K_RIGHT_DOWN;

    public int getRowChange() {
        return switch (this) {
            case UP, UP_LEFT, UP_RIGHT, K_LEFT_UP, K_RIGHT_UP -> 1;
            case DOWN, DOWN_LEFT, DOWN_RIGHT, K_LEFT_DOWN, K_RIGHT_DOWN -> -1;
            case LEFT, RIGHT -> 0;
            case K_UP_LEFT, K_UP_RIGHT -> 2;
            case K_DOWN_LEFT, K_DOWN_RIGHT -> -2;
        };
    }

    public int getColChange() {
        return switch (this) {
            case UP, DOWN -> 0;
            case LEFT, UP_LEFT, DOWN_LEFT, K_UP_LEFT, K_DOWN_LEFT -> -1;
            case RIGHT, UP_RIGHT, DOWN_RIGHT, K_UP_RIGHT, K_DOWN_RIGHT -> 1;
            case K_LEFT_UP, K_LEFT_DOWN -> -2;
            case K_RIGHT_UP, K_RIGHT_DOWN -> 2;
        };
    }
}

public class Piece implements ChessPiece {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private final ChessGame.TeamColor teamColor;
    private final PieceType pieceType;
    private final int id = counter.getAndIncrement();

    public Piece(ChessGame.TeamColor teamColor, PieceType pieceType) {
        this.teamColor = teamColor;
        this.pieceType = pieceType;
    }

    /**
     * @return Which team this chess piece belongs to
     */
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in danger
     *
     * @param board
     * @param myPosition
     * @return Collection of valid moves
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        if (board.getPiece(myPosition) == null) {
            return new HashSet<>();
        }

        return switch (pieceType) {
            case PAWN -> pawnMoves(board, myPosition);
            case ROOK -> rookMoves(board, myPosition);
            case KNIGHT -> knightMoves(board, myPosition);
            case BISHOP -> bishopMoves(board, myPosition);
            case QUEEN -> queenMoves(board, myPosition);
            case KING -> kingMoves(board, myPosition);
        };
    }

    /**
     * @return true if the position is on the board
     */
    private boolean isValidPosition(ChessPosition position) {
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }

    /**
     * @return true if the move is valid, ignoring moves into check and additional pawn rules
     */
    private boolean isValidMove(ChessBoard board, ChessPosition from, ChessPosition to) {

        // out of bounds,
        if (!isValidPosition(to)) {
            return false;
        }
        // null position or capture
        else
            return board.getPiece(to) == null || board.getPiece(to).getTeamColor() != board.getPiece(from).getTeamColor();
    }


    /**
     * @param board      the board
     * @param myPosition the position of the pawn
     * @return a set of valid moves for a pawn, excluding en passant
     */
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {

        Set<ChessMove> validMoves = new HashSet<>();

        int initialRow = (teamColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promotionRow = (teamColor == ChessGame.TeamColor.WHITE) ? 8 : 1;
        int direction = (teamColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        var forwardOne = new Position(myPosition.getRow() + direction, myPosition.getColumn());
        var forwardTwo = new Position(myPosition.getRow() + 2 * direction, myPosition.getColumn());
        var captureLeft = new Position(myPosition.getRow() + direction, myPosition.getColumn() - 1);
        var captureRight = new Position(myPosition.getRow() + direction, myPosition.getColumn() + 1);

        for (var position : new Position[]{forwardOne, forwardTwo, captureLeft, captureRight}) {
            if (isValidMove(board, myPosition, position)) {
                // block forward captures
                if ((position.equals(forwardOne) || position.equals(forwardTwo)) && board.getPiece(position) != null) {
                    continue;
                }
                // block forwardTwo if not on initial row, or validMoves is empty, meaning forwardOne was not valid
                if (position.equals(forwardTwo) && myPosition.getRow() != initialRow || (validMoves.isEmpty() && position.equals(forwardTwo))) {
                    continue;
                }
                // block capture if no piece to capture
                if ((position.equals(captureLeft) || position.equals(captureRight)) && board.getPiece(position) == null) {
                    continue;
                }
                // promotion case
                if (position.getRow() == promotionRow) {
                    for (var promotionPiece : new PieceType[]{PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                        validMoves.add(new Move(myPosition, position, promotionPiece));
                    }
                } else {
                    validMoves.add(new Move(myPosition, position));
                }
            }
        }
        return validMoves;
    }

    /**
     * Continues to add moves in a direction until there is a piece in the way or the edge of the board
     *
     * @param board      the chess board
     * @param myPosition the position of the piece
     * @param validMoves the set of valid moves to add to
     * @param direction  the initial direction of the movement
     */
    private void extendMove(ChessBoard board, ChessPosition myPosition, Set<ChessMove> validMoves, Direction direction) {
        var position = new Position(myPosition.getRow() + direction.getRowChange(), myPosition.getColumn() + direction.getColChange());
        while (isValidMove(board, myPosition, position)) {
            validMoves.add(new Move(myPosition, position));
            if (board.getPiece(position) != null && board.getPiece(myPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                break; // break if capture
            }
            position = new Position(position.getRow() + direction.getRowChange(), position.getColumn() + direction.getColChange());
        }
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        for (var direction : new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT}) {
            extendMove(board, myPosition, validMoves, direction);
        }
        return validMoves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        for (var direction : new Direction[]{Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_LEFT, Direction.DOWN_RIGHT}) {
            extendMove(board, myPosition, validMoves, direction);
        }
        return validMoves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        validMoves.addAll(rookMoves(board, myPosition));
        validMoves.addAll(bishopMoves(board, myPosition));
        return validMoves;
    }

    /**
     * Adds moves to the validMoves set if they are valid
     *
     * @param positions the positions to check, these will be validated and added to the validMoves set
     * @return validMoves the set of valid moves to add to
     */
    private Collection<ChessMove> getChessMoves(ChessBoard board, ChessPosition myPosition, Set<ChessMove> validMoves, Direction[] positions) {
        for (var position : positions) {
            if (isValidMove(board, myPosition, new Position(myPosition.getRow() + position.getRowChange(), myPosition.getColumn() + position.getColChange()))) {
                validMoves.add(new Move(myPosition, new Position(myPosition.getRow() + position.getRowChange(), myPosition.getColumn() + position.getColChange())));
            }
        }
        return validMoves;
    }

    /**
     * @return collection of moves around the king, excluding castling and moves that would put the king in check
     */
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        var positions = new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_LEFT, Direction.DOWN_RIGHT};
        return getChessMoves(board, myPosition, validMoves, positions);
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        var positions = new Direction[]{Direction.K_UP_LEFT, Direction.K_UP_RIGHT, Direction.K_DOWN_LEFT, Direction.K_DOWN_RIGHT, Direction.K_LEFT_UP, Direction.K_LEFT_DOWN, Direction.K_RIGHT_UP, Direction.K_RIGHT_DOWN};
        return getChessMoves(board, myPosition, validMoves, positions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return teamColor == piece.teamColor && pieceType == piece.pieceType && id == piece.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType, id);
    }

    @Override
    public String toString() {
        return teamColor.name() + " " + pieceType.name();
    }

}

