package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

public class ClearCommand implements Command{
    private CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) {
        return collectionManager.clearCollection();
    }

    @Override
    public String toString() {
        return "очистить коллекцию";
    }
}
