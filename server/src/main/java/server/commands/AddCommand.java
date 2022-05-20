package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

import java.io.IOException;

public class AddCommand implements Command{
    private CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) throws IOException, ClassNotFoundException {
        return collectionManager.addElement();
    }

    @Override
    public String toString() {
        return "добавить новый элемент в коллекцию";
    }
}
