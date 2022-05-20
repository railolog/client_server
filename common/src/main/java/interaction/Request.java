package interaction;

import java.io.Serializable;

public class Request implements Serializable {
    private String commandName;
    private String commandStringArgument;
    private Serializable commandObjectArgument;

    public Request(String commandName, String commandStringArgument, Serializable commandObjectArgument){
        this.commandName = commandName;
        this.commandStringArgument = commandStringArgument;
        this.commandObjectArgument = commandObjectArgument;
    }

    public Request(String commandName, String commandStringArgument){
        this(commandName, commandStringArgument, null);
    }

    public String getCommandName(){
        return commandName;
    }

    public String getCommandStringArgument() {
        return commandStringArgument;
    }

    public Serializable getCommandObjectArgument() {
        return commandObjectArgument;
    }
}
