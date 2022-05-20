package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

public class MinByCreationDateCommand implements Command{
    private CollectionManager collectionManager;

    public MinByCreationDateCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) {
        return collectionManager.minByCreationDate();
    }

    @Override
    public String toString() {
        return "вывести любой объект из коллекции, значение поля creationDate которого является минимальным";
    }
}
