package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

public class RemoveByIdCommand implements Command{
    private CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) {
        return collectionManager.removeById(arg);
    }

    @Override
    public String toString() {
        return "удалить элемент из коллекции по его id";
    }
}
