package webSocketMessages;

import webSocketMessages.userCommands.UserGameCommand;

public class ResignCommand extends UserGameCommand {
    private final String username;

    public ResignCommand(String authToken, String username) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.username = username;
    }

    public String username() {
        return username;
    }

}
