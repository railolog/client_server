package client;

import client.exceptions.ConnectToServerException;
import client.io.ConsoleIOManager;
import client.utils.ResponseHandler;
import interaction.Request;
import interaction.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

public class Client {
    private final int BUFFER_SIZE = 128;

    private ByteBuffer inputBuffer;
    private int port;
    private int reconnectionAttempts;
    private int recconectionAttemptsLimit;
    private int reconnectionTimeout;
    private ResponseHandler responseHandler;
    private Selector selector;
    private SocketChannel socketChannel;

    private ByteArrayOutputStream byteArrayOutputStream;
    private ObjectOutputStream objectOutputStream;

    public Client(int port, int reconnectionTimeout, int recconectionAttemptsLimit, ResponseHandler responseHandler) {
        this.port = port;
        this.reconnectionAttempts = 0;
        this.reconnectionTimeout = reconnectionTimeout;
        this.recconectionAttemptsLimit = recconectionAttemptsLimit;
        this.responseHandler = responseHandler;

        inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        }
        catch (IOException e){
            ConsoleIOManager.printErr(e.getMessage());
            System.exit(777);
        }
    }

    public void run(){
        boolean continueRunning = true;

        while (continueRunning) {
            try {
                connectToServer();
                continueRunning = listenServer();
            } catch (ConnectToServerException e) {
                if (reconnectionAttempts >= recconectionAttemptsLimit){
                    ConsoleIOManager.printErr("Превышено кол-во попыток подключения к серверу");
                    break;
                }
                try {
                    Thread.sleep(reconnectionTimeout);
                }
                catch (IllegalArgumentException e2){
                    ConsoleIOManager.printErr("Некорректное время ожидания: " + reconnectionTimeout);
                    break;
                }
                catch (InterruptedException e2){
                    ConsoleIOManager.printErr(e.getMessage());
                    break;
                }
            }
            reconnectionAttempts++;
        }
    }

    private boolean listenServer(){
        Request request = null;
        Response response = null;

        do {
            request = responseHandler.handleResponse(response);

            try {
                sendRequest(request);
                response = getResponse();
            }
            catch (ClassNotFoundException e){
                ConsoleIOManager.printErr("Невалидный ответ от сервера");
            }
            catch (IOException e){
                ConsoleIOManager.printErr("Произошла ошибка при сообщении с сервером");
                continue;
            }
        } while (!request.getCommandName().equals("exit"));
        return false;
    }

    private void sendRequest(Request request) throws IOException {
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                SocketChannel client = (SocketChannel) key.channel();

                if (key.isWritable()){
                    objectOutputStream.writeObject(request);
                    objectOutputStream.flush();

                    client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));

                    byteArrayOutputStream.reset();

                    return;
                }
            }
        }
    }

    private Response getResponse() throws IOException, ClassNotFoundException {
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                SocketChannel client = (SocketChannel) key.channel();

                if (key.isReadable()){
                    ArrayList<Byte> byteList = new ArrayList<Byte>();

                    inputBuffer.clear();

                    while (client.read(inputBuffer) > 0){
                        inputBuffer.flip();

                        while (inputBuffer.hasRemaining()){
                            byteList.add(inputBuffer.get());
                        }

                        inputBuffer.clear();
                    }

                    byte[] bytes = new byte[byteList.size()];

                    for (int i = 0; i < byteList.size(); i++){
                        bytes[i] = byteList.get(i);
                    }

                    ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));

                    Response response = (Response) objectInputStream.readObject();

                    objectInputStream.close();

                    return response;
                }
            }
        }
    }

    private void connectToServer() throws ConnectToServerException {
        if (reconnectionAttempts >= 1){
            ConsoleIOManager.println("Повторное подключение к серверу");
        }
        else {
            ConsoleIOManager.println("Подключение к серверу...");
        }
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost", port));
            socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);

            while (!socketChannel.finishConnect()){
                Thread.sleep(500);
            }

            ConsoleIOManager.println("Подключение успешно завершено");
        }
        catch (IOException | InterruptedException e){
            throw new ConnectToServerException();
        }
    }
}
