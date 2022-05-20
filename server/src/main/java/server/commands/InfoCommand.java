package server.commands;

import interaction.Response;
import server.collection.CollectionManager;

public class InfoCommand implements Command{
    private CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String arg) {
        return collectionManager.showCollectionInfo();
    }

    public String toString(){
        return "вывести информацию о коллекции";
    }
}
