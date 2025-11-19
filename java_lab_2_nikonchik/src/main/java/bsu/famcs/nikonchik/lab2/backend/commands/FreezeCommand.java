package bsu.famcs.nikonchik.lab2.backend.commands;

import java.util.UUID;

import bsu.famcs.nikonchik.lab2.backend.commands.policies.FreezeCommandPolicy;
import bsu.famcs.nikonchik.lab2.backend.services.AccountService;
import bsu.famcs.nikonchik.lab2.backend.commands.policies.CommandPolicy;

public class FreezeCommand extends Command {
    private final UUID accountToFreeze;
    private final AccountService accountService;

    public FreezeCommand(UUID initiatorId, UUID accountToFreeze,
                         CommandStatus status, String description,
                         AccountService accountService) {
        super(initiatorId, status, description, new FreezeCommandPolicy());
        this.accountToFreeze = accountToFreeze;
        this.accountService = accountService;
    }

    @Override
    public void execute() {

    }
}
