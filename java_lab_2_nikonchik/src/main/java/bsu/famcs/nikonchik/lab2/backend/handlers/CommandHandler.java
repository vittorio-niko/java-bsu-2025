package bsu.famcs.nikonchik.lab2.backend.handlers;

import bsu.famcs.nikonchik.lab2.backend.commands.Command;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.Actor;

public class CommandHandler {
    public void executeCommand(Command command, Actor actor) {
        if (!command.canExecuteBy(actor)) {
            throw new SecurityException(
                    "Actor does not have rights to execute the command"
            );
        }
        command.execute();
    }
}
