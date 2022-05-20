package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

public class ShuffleCommand implements Command{
    private CollectionManager collectionManager;

    public ShuffleCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) {
        return collectionManager.shuffle();
    }

    @Override
    public String toString() {
        return "перемешать элементы коллекции в случайном порядке";
    }
}
