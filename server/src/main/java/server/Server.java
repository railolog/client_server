package server;

import interaction.Request;
import interaction.Response;
import interaction.ResponseStatus;
import server.collection.CollectionManager;
import server.exceptions.ServerSocketOpeningException;
import server.io.ClientComManager;
import server.io.Console;
import server.utils.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server implements Serializable{
    private int port;
    private int soTimeout;
    private RequestHandler requestHandler;
    private ServerSocket serverSocket;
    private CollectionManager collectionManager;
    private String SAVE_PATH = "save.json";

    public Server(int port, int soTimeout, RequestHandler requestHandler, CollectionManager collectionManager){
        this.port = port;
        this.soTimeout = soTimeout;
        this.requestHandler = requestHandler;
        this.collectionManager = collectionManager;
    }

    public void run(){
        try {
            openServerSocket();
            boolean continueRunning = true;

            while (continueRunning){
                try (Socket client = serverSocket.accept()){
                    Console.print("Клиент подключен");
                    continueRunning = listenClient(client);
                }
                catch (SocketTimeoutException e){
                    Console.printErr("Сервер не дождался подключения клиента");
                    exit();
                }
                catch (IOException e){
                    Console.printErr("Не удалось завершить соединение с клиентом");
                }
            }
        }
        catch (ServerSocketOpeningException e){
            Console.printErr("Не удалось запустить сервер");
        }
    }

    private void openServerSocket() throws ServerSocketOpeningException {
        try{
            Console.print("Запуск сервера...");

            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);

            Console.print("Сервер успешно запущен");
        }
        catch (IOException e){
            Console.printErr("Не удалось запустить сервер с использованием порта " + port);
            throw new ServerSocketOpeningException();
        }
        catch (IllegalArgumentException e){
            Console.printErr("Значение порта " + port + "находится за пределами допустимых значений(0, 65535)");
            throw new ServerSocketOpeningException();
        }
    }

    private boolean listenClient(Socket client){
        Request userRequest;
        Response serverResponse = null;

        try (ClientComManager clientComManager = new ClientComManager(client)){
            client.setSoTimeout(1000);

            do {
                collectionManager.setCommunicationManager(clientComManager);

                try {
                    userRequest = clientComManager.getRequest();
                }
                catch (SocketTimeoutException e){
                    if (Console.hasNext()){
                        String command = Console.readLine();

                        if (command.equals("save")){
                            collectionManager.save(SAVE_PATH);
                        }
                        else if (command.equals("exit")){
                            exit();
                        }
                    }
                    continue;
                }

                serverResponse = requestHandler.handleRequest(userRequest);
                clientComManager.sendResponse(serverResponse);

                if (userRequest != null) {
                    Console.print("Запрос " + userRequest.getCommandName() + " успешно обработан");
                }

            } while (serverResponse == null || serverResponse.getResponseStatus() != ResponseStatus.CLIENT_EXIT);
        }
        catch (IOException e){
            /*Console.print(e.getMessage());
            System.out.println(e);
            e.printStackTrace();*/

            Console.printErr("Клиент отсоединился\nОжидание повторного подключения");
        }
        catch (ClassNotFoundException e) {
            Console.printErr("Некорректный запрос от клиента");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Console.print("Клиент отключился, ожидаем повторного подключения");
        return true;
    }

    public void exit(){
        collectionManager.save(SAVE_PATH);
        System.exit(0);
    }
}
