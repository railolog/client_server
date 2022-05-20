package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

public class ReorderCommand implements Command{
    private CollectionManager collectionManager;

    public ReorderCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) {
        return collectionManager.reorder();
    }

    @Override
    public String toString() {
        return "отсортировать коллекцию в порядке, обратном нынешнему";
    }
}
