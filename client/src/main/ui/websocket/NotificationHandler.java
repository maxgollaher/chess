package ui.websocket;

import webSocketMessages.ErrorMessage;
import webSocketMessages.LoadGameMessage;
import webSocketMessages.Notification;

public interface NotificationHandler {
    void notify(Notification serverMessage);
    void loadGame(LoadGameMessage loadGameMessage);
    void error(ErrorMessage errorMessage);
}
