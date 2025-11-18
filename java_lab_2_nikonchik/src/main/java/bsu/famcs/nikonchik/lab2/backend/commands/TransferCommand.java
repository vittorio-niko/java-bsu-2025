package bsu.famcs.nikonchik.lab2.backend.commands;

import java.util.UUID;
import java.math.BigDecimal;

import bsu.famcs.nikonchik.lab2.backend.services.AccountService;
import bsu.famcs.nikonchik.lab2.backend.entities.actions.Transaction;
import bsu.famcs.nikonchik.lab2.backend.exceptions.CommandExceptions.*;

public class TransferCommand implements Command {
    private final UUID commandId;
    private final UUID fromAccountId;
    private final UUID toAccountId;
    private final BigDecimal amount;
    private final String description;
    private final AccountService accountService;
    private CommandStatus status;
    private UUID transactionId;

    public TransferCommand(UUID fromAccountId, UUID toAccountId,
                           BigDecimal amount, String description,
                           AccountService accountService) {
        this.commandId = UUID.randomUUID();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.description = description;
        this.accountService = accountService;
        this.status = CommandStatus.PENDING;
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

    @Override
    public CommandStatus getStatus() {
        return status;
    }

    public UUID getCommandId() { return commandId; }
    public UUID getFromAccountId() { return fromAccountId; }
    public UUID getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public UUID getTransactionId() { return transactionId; }
}
