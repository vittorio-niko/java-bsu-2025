package bsu.famcs.nikonchik.lab2.backend.commands;

import java.util.UUID;

import bsu.famcs.nikonchik.lab2.backend.commands.policies.FreezeCommandPolicy;
import bsu.famcs.nikonchik.lab2.backend.services.AccountService;
import bsu.famcs.nikonchik.lab2.backend.entities.events.AccountFreezeEvent;
import bsu.famcs.nikonchik.lab2.backend.exceptions.CommandExceptions.CommandExecutionException;

public class FreezeCommand extends Command {
    private final UUID accountToFreeze;
    private final AccountService accountService;
    private UUID accountFreezeEventId;

    public FreezeCommand(UUID initiatorId, UUID accountToFreeze,
                         CommandStatus status, String description,
                         AccountService accountService) {
        super(initiatorId, status, description, new FreezeCommandPolicy());
        this.accountToFreeze = accountToFreeze;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        try {
            AccountFreezeEvent event = accountService.freezeAccount(
                    accountToFreeze, description
            );
            this.accountFreezeEventId = event.getEventId();
            this.status = CommandStatus.EXECUTED;
        } catch (Exception e) {
            this.status = CommandStatus.FAILED;
            throw new CommandExecutionException("Freeze action failed", e);
        }
    }
}
