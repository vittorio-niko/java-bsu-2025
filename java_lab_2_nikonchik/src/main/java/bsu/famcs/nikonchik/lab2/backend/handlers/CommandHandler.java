package bsu.famcs.nikonchik.lab2.backend.handlers;

import bsu.famcs.nikonchik.lab2.backend.commands.Command;

public class CommandHandler {
    public void executeCommand(Command command) {
        command.execute();
    }
}
