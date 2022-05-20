package server.io;

import core.City;
import interaction.Request;
import interaction.Response;
import interaction.ResponseStatus;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientComManager implements AutoCloseable{
    private final ObjectInputStream objectInputStream;
    private final OutputStream out;

    public ClientComManager(Socket socket) throws IOException {
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        out = socket.getOutputStream();
    }

    public Request getRequest() throws IOException, ClassNotFoundException {
        return (Request) objectInputStream.readObject();
    }

    public void sendResponse(Response response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(response);
        objectOutputStream.flush();

        out.write(byteArrayOutputStream.toByteArray());
        out.flush();

        objectOutputStream.close();
        byteArrayOutputStream.close();
    }

    public City readCity() throws IOException, ClassNotFoundException {
        sendResponse(new Response(ResponseStatus.READ_ELEM));
        Request request = null;

        while (request == null){
            try {
                request = getRequest();
            }
            catch (SocketTimeoutException ignored){}
        }

        if (request.getCommandObjectArgument() != null){
            return (City) request.getCommandObjectArgument();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        objectInputStream.close();
        out.close();
    }
}
