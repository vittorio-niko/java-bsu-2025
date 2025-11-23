package bsu.famcs.nikonchik.lab2.backend.services;

import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bsu.famcs.nikonchik.lab2.backend.factories.EventErrorLogFactory;
import bsu.famcs.nikonchik.lab2.backend.factories.TransactionFactory;
import bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents.*;
import bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents.Transaction.TransactionStatus;
import bsu.famcs.nikonchik.lab2.backend.entities.products.Account;
import bsu.famcs.nikonchik.lab2.backend.exceptions.AccountExceptions.*;
import bsu.famcs.nikonchik.lab2.backend.entities.events.lifecycleevents.AccountFreezeEvent;
import bsu.famcs.nikonchik.lab2.backend.entities.events.lifecycleevents.AccountFreezeEvent.ActionType;
import bsu.famcs.nikonchik.lab2.backend.repositories.*;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountFreezeActionsRepository accountFreezeActionsRepository;
    private final MoneyFlowErrorsRepository moneyFlowErrorsRepository;
    private final LifecycleErrorsRepository lifecycleErrorsRepository;

    private final TransactionFactory transactionFactory;
    private final EventErrorLogFactory eventErrorLogFactory;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          AccountFreezeActionsRepository accountFreezeActionsRepository,
                          MoneyFlowErrorsRepository moneyFlowErrorsRepository,
                          LifecycleErrorsRepository lifecycleErrorsRepository,
                          TransactionFactory transactionFactory,
                          EventErrorLogFactory eventErrorLogFactory) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountFreezeActionsRepository = accountFreezeActionsRepository;
        this.moneyFlowErrorsRepository = moneyFlowErrorsRepository;
        this.lifecycleErrorsRepository = lifecycleErrorsRepository;
        this.transactionFactory = transactionFactory;
        this.eventErrorLogFactory = eventErrorLogFactory;
    }

    @Transactional
    public TransferTransaction transferFunds(UUID initiatorId, UUID fromAccountId,
                                             UUID toAccountId, BigDecimal amount,
                                             String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        validateAccountActive(fromAccount);

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));
        validateAccountActive(toAccount);

        TransferTransaction transaction = transactionFactory.createTransfer(
                initiatorId, fromAccountId, toAccountId, amount, description
        );

        validateForSufficientFunds(fromAccount, amount);

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
        transaction.setStatus(TransactionStatus.COMPLETED);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        transactionRepository.save(transaction);

        return transaction;
    }

    @Transactional
    public DepositTransaction depositFunds(UUID initiatorId, UUID toAccountId,
                                           BigDecimal amount, String description,
                                           String depositMethod) {
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));
        validateAccountActive(toAccount);

        DepositTransaction transaction = transactionFactory.createDeposit(
                initiatorId, toAccountId, amount, description, depositMethod
        );

        toAccount.deposit(amount);
        transaction.setStatus(TransactionStatus.COMPLETED);

        accountRepository.save(toAccount);
        transactionRepository.save(transaction);

        return transaction;
    }

    @Transactional
    public WithdrawalTransaction withdrawFunds(UUID initiatorId, UUID fromAccountId,
                                               BigDecimal amount, String description,
                                               String withdrawalLocation) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        validateAccountActive(fromAccount);

        WithdrawalTransaction transaction = transactionFactory.createWithdrawal(
                initiatorId, fromAccountId, amount, description, withdrawalLocation
        );

        validateForSufficientFunds(fromAccount, amount);

        fromAccount.withdraw(amount);
        transaction.setStatus(TransactionStatus.COMPLETED);

        accountRepository.save(fromAccount);
        transactionRepository.save(transaction);

        return transaction;
    }

    @Transactional
    public AccountFreezeEvent freezeAccount(UUID initiatorId, UUID accountToFreeze,
                                            String description) {
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
                initiatorId,
                LocalDateTime.now(),
                description,
                ActionType.FREEZE
        );

        accountFreezeActionsRepository.save(event);

        return event;
    }

    @Transactional
    public AccountFreezeEvent unfreezeAccount(UUID initiatorId, UUID accountToUnfreeze, String description) {
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
                initiatorId,
                LocalDateTime.now(),
                description,
                ActionType.UNFREEZE
        );

        accountFreezeActionsRepository.save(event);

        return event;
    }

    private void validateForSufficientFunds(Account fromAccount, BigDecimal amount) {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

    private void validateAccountActive(Account account) {
        if (account.isFrozen()) {
            throw new AccountNotActiveException(account.getAccountNumber(),
                    account.getStatus().toString());
        }
    }
}

