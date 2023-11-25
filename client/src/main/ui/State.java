package ui;

public enum State {
    SIGNED_OUT,
    SIGNED_IN;

    @Override
    public String toString() {
        return switch (this) {
            case SIGNED_OUT -> "Signed Out";
            case SIGNED_IN -> "Signed In";
        };
    }
}