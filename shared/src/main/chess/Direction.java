package chess;

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
