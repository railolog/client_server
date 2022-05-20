package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

import java.io.IOException;

public class UpdateCommand implements Command{
    private CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) throws IOException, ClassNotFoundException {
        return collectionManager.update(arg);
    }

    @Override
    public String toString() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
