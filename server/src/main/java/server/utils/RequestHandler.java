package server.utils;

import interaction.Request;
import interaction.Response;
import interaction.ResponseStatus;

import java.io.IOException;

public class RequestHandler {
    CommandManager commandManager;

    public RequestHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Response handleRequest(Request request) throws IOException, ClassNotFoundException {
        if (request == null){
            return new Response(ResponseStatus.ERROR);
            // TODO
        }
        else if (request.getCommandObjectArgument() != null){
            return new Response(ResponseStatus.ERROR);
            // TODO
        }
        else if (request.getCommandName().equals("exit")){
            return new Response(ResponseStatus.CLIENT_EXIT);
        }
        else {
            return commandManager.execute(request.getCommandName(), request.getCommandStringArgument());
        }
    }
}
