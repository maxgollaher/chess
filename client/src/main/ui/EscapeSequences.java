package ui;

/**
 * This class contains constants and functions relating to ANSI Escape Sequences that are useful in the Client display
 */
public class EscapeSequences {

    static final String UNICODE_ESCAPE = "\u001b";
    private static final String ANSI_ESCAPE = "\033";

    public static final String ERASE_SCREEN = UNICODE_ESCAPE + "[H" + UNICODE_ESCAPE + "[2J";
    public static final String ERASE_LINE = UNICODE_ESCAPE + "[2K";

    public static final String BOLD = UNICODE_ESCAPE + "[1m";
    public static final String FAINT = UNICODE_ESCAPE + "[2m";
    public static final String BOLD_FAINT = UNICODE_ESCAPE + "[22m";
    public static final String SET_TEXT_ITALIC = UNICODE_ESCAPE + "[3m";
    public static final String RESET_TEXT_ITALIC = UNICODE_ESCAPE + "[23m";
    public static final String SET_TEXT_UNDERLINE = UNICODE_ESCAPE + "[4m";
    public static final String RESET_TEXT_UNDERLINE = UNICODE_ESCAPE + "[24m";
    public static final String SET_TEXT_BLINKING = UNICODE_ESCAPE + "[5m";
    public static final String RESET_TEXT_BLINKING = UNICODE_ESCAPE + "[25m";

    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    private static final String SET_BG_COLOR = UNICODE_ESCAPE + "[48;5;";

    public static final String BLACK = SET_TEXT_COLOR + "0m";
    public static final String LIGHT_GREY = SET_TEXT_COLOR + "242m";
    public static final String DARK_GRAY = SET_TEXT_COLOR + "235m";
    public static final String RED = UNICODE_ESCAPE + "[91m";
    public static final String GREEN = SET_TEXT_COLOR + "46m";
    public static final String YELLOW = SET_TEXT_COLOR + "226m";
    public static final String BLUE = SET_TEXT_COLOR + "12m";
    public static final String MAGENTA = SET_TEXT_COLOR + "5m";
    public static final String WHITE = SET_TEXT_COLOR + "15m";
    public static final String RESET_TEXT_COLOR = SET_TEXT_COLOR + "0m";

    public static final String BG_BLACK = SET_BG_COLOR + "0m";
    public static final String BG_LIGHT_GRAY = SET_BG_COLOR + "242m";
    public static final String BG_DARK_GRAY = SET_BG_COLOR + "235m";
    public static final String BG_RED = SET_BG_COLOR + "160m";
    public static final String BG_GREEN = SET_BG_COLOR + "46m";
    public static final String BG_DARK_GREEN = SET_BG_COLOR + "22m";
    public static final String BG_YELLOW = SET_BG_COLOR + "226m";
    public static final String BG_BLUE = SET_BG_COLOR + "12m";
    public static final String BG_MAGENTA = SET_BG_COLOR + "5m";
    public static final String BG_WHITE = SET_BG_COLOR + "15m";
    public static final String RESET_BG_COLOR = UNICODE_ESCAPE + "[0m";
    public static final String EMPTY = " \u2003 ";

    public static String moveCursorToLocation(int x, int y) { return UNICODE_ESCAPE + "[" + y + ";" + x + "H"; }
}
