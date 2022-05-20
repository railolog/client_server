package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

public class GroupCountingCommand implements Command{
    private CollectionManager collectionManager;

    public GroupCountingCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) {
        return collectionManager.groupCountingByCoordinates();
    }

    @Override
    public String toString() {
        return "сгруппировать элементы коллекции по значению поля coordinates";
    }
}
