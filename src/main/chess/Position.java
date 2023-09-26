package chess;

public class Position implements ChessPosition {

    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row - 1;
        this.column = column - 1;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    @Override
    public int getColumn() {
        return column;
    }
}
