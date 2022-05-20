package server.commands;

import interaction.Response;

import java.io.IOException;

public interface Command {
    Response execute(String arg) throws IOException, ClassNotFoundException;
}
