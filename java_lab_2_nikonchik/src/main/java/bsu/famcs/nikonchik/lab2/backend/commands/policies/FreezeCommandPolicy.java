package bsu.famcs.nikonchik.lab2.backend.commands.policies;

import bsu.famcs.nikonchik.lab2.backend.commands.Command;
import bsu.famcs.nikonchik.lab2.backend.commands.TransferCommand;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.Actor;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.PermissionLevel;

public class FreezeCommandPolicy implements CommandPolicy {
    @Override
    public boolean canExecute(Actor actor, Command command) {
        if (!(command instanceof FreezeCommand)) return false;


    }
}
