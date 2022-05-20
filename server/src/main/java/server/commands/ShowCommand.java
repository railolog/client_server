package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

public class ShowCommand implements Command{
    private CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) {
        return collectionManager.printElements();
    }

    @Override
    public String toString(){
        return "вывести все элементы коллекции";
    }
}
