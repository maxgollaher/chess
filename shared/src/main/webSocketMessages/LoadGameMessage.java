package webSocketMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class LoadGameMessage extends ServerMessage {
    private final String message;

    public LoadGameMessage(String message) {
        super(ServerMessageType.LOAD_GAME);
        this.message = message;
    }

    public String message() {
        return message;
    }
}
