package client;

import client.utils.ResponseHandler;

public class Main {
    public static void main(String[] args) {
        ResponseHandler responseHandler = new ResponseHandler();
        Client client = new Client(1337, 500, 100, responseHandler);
        client.run();
    }
}
