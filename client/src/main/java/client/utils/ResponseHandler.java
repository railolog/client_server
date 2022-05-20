package client.utils;

import client.io.ConsoleIOManager;
import interaction.Request;
import interaction.Response;
import interaction.ResponseStatus;

public class ResponseHandler {

    public Request handleResponse(Response response){
        if (response == null || response.getResponseStatus() == ResponseStatus.SUCCESS){
            try {
                assert response != null;
                ConsoleIOManager.println(response.getResponseBody());
            }
            catch (NullPointerException ignored){
            }

            CommandWrapper command = ConsoleIOManager.readCommand();
            return new Request(command.getCommand(), command.getArgument());
        }
        else if (response.getResponseStatus() == ResponseStatus.READ_ELEM){
            return new Request("", null, ConsoleIOManager.readCity());
        }
        else if (response.getResponseStatus() == ResponseStatus.EXECUTE_SCRIPT){
            String path = response.getResponseBody();

            if (path == null || path.length() == 0){
                ConsoleIOManager.printErr("Не введен путь к файлу");
                return handleResponse(null);
            }

            ConsoleIOManager.setFileInput(path);
            return handleResponse(null);
        }
        else if (response.getResponseStatus() == ResponseStatus.ERROR){
            if (response.getResponseBody() != null){
                ConsoleIOManager.printErr(response.getResponseBody());
            }
            else {
                ConsoleIOManager.printErr("непредвиденная ошибка");
            }

            CommandWrapper command = ConsoleIOManager.readCommand();
            return new Request(command.getCommand(), command.getArgument());
        }
        else{
            return null;
        }
    }
}
