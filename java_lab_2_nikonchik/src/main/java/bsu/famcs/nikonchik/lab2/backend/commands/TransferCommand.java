package bsu.famcs.nikonchik.lab2.backend.commands;

import java.util.UUID;
import java.math.BigDecimal;

import bsu.famcs.nikonchik.lab2.backend.commands.policies.TransferCommandPolicy;
import bsu.famcs.nikonchik.lab2.backend.services.AccountService;
import bsu.famcs.nikonchik.lab2.backend.entities.events.Transaction;
import bsu.famcs.nikonchik.lab2.backend.exceptions.CommandExceptions.CommandExecutionException;

public class TransferCommand extends Command {
    private final UUID fromAccountId;
    private final UUID toAccountId;
    private final BigDecimal amount;
    private final AccountService accountService;
    private UUID transactionId;

    public TransferCommand(UUID initiatorId, UUID fromAccountId,
                           UUID toAccountId, BigDecimal amount,
                           String description, AccountService accountService) {
        super(initiatorId, CommandStatus.PENDING,
                description, new TransferCommandPolicy());
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        try {
            Transaction transaction = accountService.transferFunds(
                    fromAccountId, toAccountId, amount, description
            );
            this.transactionId = transaction.getId();
            this.status = CommandStatus.EXECUTED;
        } catch (Exception e) {
            this.status = CommandStatus.FAILED;
            throw new CommandExecutionException("Transfer failed", e);
        }
    }
}
