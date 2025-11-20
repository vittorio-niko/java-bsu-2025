package bsu.famcs.nikonchik.lab2.backend.services;

import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bsu.famcs.nikonchik.lab2.backend.entities.events.Transaction;
import bsu.famcs.nikonchik.lab2.backend.entities.events.Transaction.*;
import bsu.famcs.nikonchik.lab2.backend.entities.products.Account;
import bsu.famcs.nikonchik.lab2.backend.exceptions.AccountExceptions.*;
import bsu.famcs.nikonchik.lab2.backend.entities.events.AccountFreezeEvent;
import bsu.famcs.nikonchik.lab2.backend.entities.events.AccountFreezeEvent.ActionType;
import bsu.famcs.nikonchik.lab2.backend.repositories.*;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountFreezeActionsRepository accountFreezeActionsRepository;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          AccountFreezeActionsRepository accountFreezeActionsRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountFreezeActionsRepository = accountFreezeActionsRepository;
    }

    @Transactional
    public Transaction transferFunds(UUID fromAccountId, UUID toAccountId,
                                     BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                TransactionType.TRANSFER,
                amount,
                LocalDateTime.now(),
                TransactionStatus.PENDING,
                description,
                fromAccountId,
                toAccountId
        ); //заменить на фабрику

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        transaction.setStatus(TransactionStatus.COMPLETED);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        transactionRepository.save(transaction);

        return transaction;
    }

    @Transactional
    public AccountFreezeEvent freezeAccount(UUID accountToFreeze, String description) {
        Account account = accountRepository.findById(accountToFreeze)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found: " + accountToFreeze));

        if (account.isFrozen()) {
            throw new IllegalStateException("Account is already frozen: " + accountToFreeze);
        }

        account.freeze();
        accountRepository.save(account);

        AccountFreezeEvent event = new AccountFreezeEvent(
                UUID.randomUUID(),
                accountToFreeze,
                LocalDateTime.now(),
                description,
                ActionType.FREEZE
        );

        accountFreezeActionsRepository.save(event);

        return event;
    }

    @Transactional
    public AccountFreezeEvent unfreezeAccount(UUID accountToUnfreeze, String description) {
        Account account = accountRepository.findById(accountToUnfreeze)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found: " + accountToUnfreeze));

        if (account.isActive()) {
            throw new IllegalStateException("Account is already active: " + accountToUnfreeze);
        }

        account.activate();
        accountRepository.save(account);

        AccountFreezeEvent event = new AccountFreezeEvent(
                UUID.randomUUID(),
                accountToUnfreeze,
                LocalDateTime.now(),
                description,
                ActionType.UNFREEZE
        );

        accountFreezeActionsRepository.save(event);

        return event;
    }
}

