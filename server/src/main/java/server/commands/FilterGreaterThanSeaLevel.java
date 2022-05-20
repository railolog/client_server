package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

public class FilterGreaterThanSeaLevel implements Command{
    private CollectionManager collectionManager;

    public FilterGreaterThanSeaLevel(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) {
        return collectionManager.filterGreaterThanSeaLevel(arg);
    }

    @Override
    public String toString() {
        return "вывести элементы, значение поля metersAboveSeaLevel которых больше заданного";
    }
}
