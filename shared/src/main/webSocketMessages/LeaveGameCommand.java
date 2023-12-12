package webSocketMessages;

import webSocketMessages.userCommands.UserGameCommand;

public class LeaveGameCommand extends UserGameCommand {
    private final String username;

    public LeaveGameCommand(String authToken, String username) {
        super(authToken);
        this.commandType = UserGameCommand.CommandType.LEAVE;
        this.username = username;
    }

    public String username() {
        return username;
    }

}
