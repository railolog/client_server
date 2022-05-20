package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

import java.io.IOException;

public class AddIfMaxCommand implements Command{
    private CollectionManager collectionManager;

    public AddIfMaxCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) throws IOException, ClassNotFoundException {
        return collectionManager.addIfMax();
    }

    @Override
    public String toString() {
        return "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }
}
