package webSocketMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class LoadGameMessage extends ServerMessage {
    private final String game;

    public LoadGameMessage(String message) {
        super(ServerMessageType.LOAD_GAME);
        this.game = message;
    }

    public String game() {
        return game;
    }
}
