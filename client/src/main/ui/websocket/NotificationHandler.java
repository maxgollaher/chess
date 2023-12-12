package ui.websocket;

import webSocketMessages.LoadGameMessage;
import webSocketMessages.Notification;

public interface NotificationHandler {
    void notify(Notification serverMessage);
    void loadGame(LoadGameMessage loadGameMessage);
}
