package server;

import com.google.gson.JsonParseException;
import server.collection.CityCollectionManager;
import server.commands.*;
import server.io.Console;
import server.io.FileManager;
import server.utils.CommandManager;
import server.utils.RequestHandler;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        int PORT = 1337;

        CityCollectionManager collectionManager = new CityCollectionManager();

        CommandManager commandManager = new CommandManager();

        commandManager.addCommand("add", new AddCommand(collectionManager));
        commandManager.addCommand("add_if_max", new AddIfMaxCommand(collectionManager));
        commandManager.addCommand("clear", new ClearCommand(collectionManager));
        commandManager.addCommand("filter_greater_than_meters_above_sea_level", new FilterGreaterThanSeaLevel(collectionManager));
        commandManager.addCommand("group_counting_by_coordinates", new GroupCountingCommand(collectionManager));
        commandManager.addCommand("info", new InfoCommand(collectionManager));
        commandManager.addCommand("min_by_creation_date", new MinByCreationDateCommand(collectionManager));
        commandManager.addCommand("remove_by_id", new RemoveByIdCommand(collectionManager));
        commandManager.addCommand("reorder", new ReorderCommand(collectionManager));
        commandManager.addCommand("show", new ShowCommand(collectionManager));
        commandManager.addCommand("shuffle", new ShuffleCommand(collectionManager));
        commandManager.addCommand("update", new UpdateCommand(collectionManager));

        RequestHandler requestHandler = new RequestHandler(commandManager);
        Server server = new Server(1337, 15 * 1000, requestHandler, collectionManager);

        if (args.length > 0){
            String path = args[0].trim();

            try {
                collectionManager.setCollection(new FileManager().load(path));
                if (args.length > 1){
                    try {
                        PORT = Integer.parseInt(args[1]);
                    }
                    catch (Exception ignored){}
                }
            }
            catch (FileNotFoundException | NullPointerException | JsonParseException e){
                Console.printErr(e.getMessage() + "\nНе удалось загрузить коллекцию\nЗавершение работы");
                return;
            }
            server.run();
        }
        else {
            Console.printErr("не передан путь к файлу\nЗавершение работы");
        }
    }
}
