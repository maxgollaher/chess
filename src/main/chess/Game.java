package chess;

import java.security.Permission;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static chess.ChessPiece.PieceType.KING;
import static chess.ChessPiece.PieceType.ROOK;
import static java.lang.Math.abs;

public class Game implements ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    private ChessPiece isEnPassant;
    private Set<ChessPiece> hasMoved = new HashSet<>();

    /**
     * @return Which team's turn it is
     */
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at startPosition
     */
    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        var piece = board.getPiece(startPosition);
        Set<ChessMove> validMoves = new HashSet<>(piece.pieceMoves(board, startPosition));
        var enPassantMove = enPassantMoves(startPosition);
        if (enPassantMove != null) {
            validMoves.add(enPassantMove);
        }
        Set<ChessMove> castleMove = new HashSet<>(castleMoves(startPosition));
        if (!castleMove.isEmpty()) {
            validMoves.addAll(castleMove);
        }
        // always check if moves put the king into check
        removeIfCheck(validMoves);
        return validMoves;
    }

    private void removeIfCheck(Set<ChessMove> validMoves) {
        validMoves.removeIf(move -> {
            // swap places, call isInCheck, remove if in check after swapping back.
            var fromPiece = board.getPiece(move.getStartPosition());
            var toPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), fromPiece);
            board.addPiece(move.getStartPosition(), null);
            boolean isInCheck = isInCheck(fromPiece.getTeamColor());
            board.addPiece(move.getStartPosition(), fromPiece);
            board.addPiece(move.getEndPosition(), toPiece);
            return isInCheck;
        });
    }

    private Collection<ChessMove> castleMoves(ChessPosition startPosition) {
        var piece = board.getPiece(startPosition);

        // if the piece is not a king or the king has moved, no castling
        if (piece.getPieceType() != KING || hasMoved.contains(piece)) {
            return new HashSet<>();
        }

        // if for some reason the king is not on the correct position, no castling
        // this only matters for testing purposes
        var kingRow = piece.getTeamColor() == TeamColor.WHITE ? 1 : 8;
        if (startPosition.getColumn() != 5 || startPosition.getRow() != kingRow) {
            return new HashSet<>();
        }

        // if the king is in check, no castling
        if (isInCheck(piece.getTeamColor())) {
            return new HashSet<>();
        }

        var baseRow = startPosition.getRow();
        var kingCol = startPosition.getColumn();
        var leftRook = board.getPiece(new Position(baseRow, 1));
        var rightRook = board.getPiece(new Position(baseRow, 8));

        // check for empty spaces between king and leftRook
        boolean leftEmpty = true;
        boolean rightEmpty = true;
        for (int i = 2; i < kingCol; i++) {
            if (board.getPiece(new Position(baseRow, i)) != null) {
                leftEmpty = false;
            }
        }
        for (int i = 7; i > kingCol; i--) {
            if (board.getPiece(new Position(baseRow, i)) != null) {
                rightEmpty = false;
            }
        }
        // if both sides are not empty, don't continue
        if (!leftEmpty && !rightEmpty) {
            return new HashSet<>();
        }
        Set<ChessMove> shortMoves = new HashSet<>();
        var leftMove = new Move(startPosition, new Position(baseRow, kingCol - 1));
        var rightMove = new Move(startPosition, new Position(baseRow, kingCol + 1));
        if (leftEmpty) {
            shortMoves.add(leftMove);
        }
        if (rightEmpty) {
            shortMoves.add(rightMove);
        }
        removeIfCheck(shortMoves);
        if (shortMoves.isEmpty()) {
            return new HashSet<>();
        }

        Set<ChessMove> result = new HashSet<>();
        if (shortMoves.contains(leftMove) && !hasMoved.contains(leftRook)) {
            result.add(new Move(startPosition, new Position(baseRow, kingCol - 2)));
        }
        if (shortMoves.contains(rightMove) && !hasMoved.contains(rightRook)) {
            result.add(new Move(startPosition, new Position(baseRow, kingCol + 2)));
        }
        return result;
    }

    /**
     * @param startPosition the piece to get valid moves for
     * @return a valid enPassant move
     */
    private ChessMove enPassantMoves(ChessPosition startPosition) {
        if (isEnPassant != null) {
            var enPassantRow = isEnPassant.getTeamColor() == TeamColor.WHITE ? 4 : 5;
            if (startPosition.getRow() == enPassantRow) {
                var left = new Position(startPosition.getRow(), startPosition.getColumn() + Direction.LEFT.getColChange());
                var right = new Position(startPosition.getRow(), startPosition.getColumn() + Direction.RIGHT.getColChange());
                var direction = isEnPassant.getTeamColor() == TeamColor.WHITE ? -1 : 1;
                if (isValidPosition(left) && board.getPiece(left) == isEnPassant) {
                    return new Move(startPosition, new Position(startPosition.getRow() + direction * Direction.UP_LEFT.getRowChange(), startPosition.getColumn() + Direction.UP_LEFT.getColChange()));
                } else if (isValidPosition(right) && board.getPiece(right) == isEnPassant) {
                    return new Move(startPosition, new Position(startPosition.getRow() + direction * Direction.UP_RIGHT.getRowChange(), startPosition.getColumn() + Direction.UP_RIGHT.getColChange()));
                }
            }
        }
        return null;
    }

    /**
     * @return true if the position is on the board
     */
    private boolean isValidPosition(ChessPosition position) {
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }

    /**
     * @param teamColor the specified color for the king
     * @return the king's position
     */
    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var position = new Position(i, j);
                var piece = board.getPiece(position);
                if (piece == null) continue;
                if (piece.getPieceType().equals(KING) && piece.getTeamColor().equals(teamColor)) {
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {

        // check first if it is the correct turn
        if (board.getPiece(move.getStartPosition()).getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Incorrect team turn");
        }
        Set<ChessMove> validMoves = new HashSet<>(validMoves(move.getStartPosition()));
        if (validMoves.contains(move)) {
            var originalPiece = board.getPiece(move.getStartPosition());
            var promotionPiece = move.getPromotionPiece();

            // add the piece to the hasMoved set, preventing castling
            if (originalPiece.getPieceType() == ChessPiece.PieceType.KING ||
                    originalPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
                hasMoved.add(originalPiece);
            }

            handleEnPassant(move, originalPiece);
            handleCastle(move, originalPiece);

            if (promotionPiece == null) {
                board.addPiece(move.getEndPosition(), originalPiece);
            } else {
                // add the new piece to the hasMoved set, preventing castling with promoted rooks
                hasMoved.remove(originalPiece);
                var pawnPromotionPiece = new Piece(teamTurn, promotionPiece);
                hasMoved.add(pawnPromotionPiece);
                board.addPiece(move.getEndPosition(), pawnPromotionPiece);
            }
            board.addPiece(move.getStartPosition(), null);
            teamTurn = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        } else {
            throw new InvalidMoveException();
        }
    }

    private void handleCastle(ChessMove move, ChessPiece originalPiece) {
        if (originalPiece.getPieceType() == KING) {
            if (move.getEndPosition().getColumn() == 7) {
                board.addPiece(new Position(move.getStartPosition().getRow(), 6), board.getPiece(new Position(move.getStartPosition().getRow(), 1)));
                hasMoved.add(board.getPiece(new Position(move.getStartPosition().getRow(), 6)));
                board.addPiece(new Position(move.getStartPosition().getRow(), 8), null);
            } else if (move.getEndPosition().getColumn() == 3) {
                board.addPiece(new Position(move.getStartPosition().getRow(), 4), board.getPiece(new Position(move.getStartPosition().getRow(), 8)));
                hasMoved.add(board.getPiece(new Position(move.getStartPosition().getRow(), 4)));
                board.addPiece(new Position(move.getStartPosition().getRow(), 1), null);
            }
        }
    }

    private void handleEnPassant(ChessMove move, ChessPiece originalPiece) {
        if (originalPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            var direction = originalPiece.getTeamColor() == TeamColor.WHITE ? 1 : -1;
            if (this.isEnPassant != null && board.getPiece(new Position(move.getEndPosition().getRow() - direction, move.getEndPosition().getColumn())) == isEnPassant) {
                board.addPiece(new Position(move.getEndPosition().getRow() - direction, move.getEndPosition().getColumn()), null);
            }
        }
        // only set en passant for one turn
        if (originalPiece.getPieceType() == ChessPiece.PieceType.PAWN &&
                (abs(move.getStartPosition().getRow() - move.getEndPosition().getRow()) == 2)) {
            this.isEnPassant = originalPiece;
        } else {
            this.isEnPassant = null;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    @Override
    public boolean isInCheck(TeamColor teamColor) {

        var kingPosition = getKingPosition(teamColor);

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var curPos = new Position(i, j);
                var piece = board.getPiece(curPos);
                if (piece == null) continue;
                if (!piece.getTeamColor().equals(teamColor)) {
                    Set<ChessMove> validMoves = new HashSet<>(piece.pieceMoves(board, curPos));
                    for (var move : validMoves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        var kingPosition = getKingPosition(teamColor);
        return isInCheck(teamColor) && validMoves(kingPosition).isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var curPos = new Position(i, j);
                var piece = board.getPiece(curPos);
                if (piece == null) continue;
                if (piece.getTeamColor().equals(teamColor)) {
                    Set<ChessMove> validMoves = new HashSet<>(validMoves(curPos));
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
        hasMoved = new HashSet<>(); // reset hasMoved for testing purposes
        isEnPassant = null;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
