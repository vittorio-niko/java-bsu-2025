package bsu.famcs.nikonchik.lab2.backend.services;

import java.util.UUID;
import java.math.BigDecimal;

import bsu.famcs.nikonchik.lab2.backend.entities.logs.lifecycleerrorlogs.FreezeErrorLog;
import bsu.famcs.nikonchik.lab2.backend.entities.logs.moneyflowerrorlogs.MoneyFlowErrorLog;
import bsu.famcs.nikonchik.lab2.backend.factories.LifecycleEventsFactory;
import bsu.famcs.nikonchik.lab2.backend.repositories.errorlogrepositories.LifecycleErrorsRepository;
import bsu.famcs.nikonchik.lab2.backend.repositories.errorlogrepositories.MoneyFlowErrorsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bsu.famcs.nikonchik.lab2.backend.factories.EventErrorLogFactory;
import bsu.famcs.nikonchik.lab2.backend.factories.TransactionFactory;
import bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents.*;
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
    private final LifecycleEventsFactory lifecycleEventsFactory;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          AccountFreezeActionsRepository accountFreezeActionsRepository,
                          MoneyFlowErrorsRepository moneyFlowErrorsRepository,
                          LifecycleErrorsRepository lifecycleErrorsRepository,
                          TransactionFactory transactionFactory,
                          EventErrorLogFactory eventErrorLogFactory,
                          LifecycleEventsFactory lifecycleEventsFactory) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountFreezeActionsRepository = accountFreezeActionsRepository;
        this.moneyFlowErrorsRepository = moneyFlowErrorsRepository;
        this.lifecycleErrorsRepository = lifecycleErrorsRepository;
        this.transactionFactory = transactionFactory;
        this.eventErrorLogFactory = eventErrorLogFactory;
        this.lifecycleEventsFactory = lifecycleEventsFactory;
    }

    @Transactional
    public TransferTransaction transferFunds(UUID initiatorId, UUID fromAccountId,
                                             UUID toAccountId, BigDecimal amount,
                                             String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        TransferTransaction transaction = transactionFactory.createTransfer(
                initiatorId, fromAccountId, toAccountId, amount, description
        );

        try {
            validateAccountActive(fromAccount);
            validateAccountActive(toAccount);
            validateForSufficientFunds(fromAccount, amount);

            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            transaction.setCompleted();

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            transactionRepository.save(transaction);
        } catch (Exception e) {
            transaction.setFailed(e);
            transactionRepository.save(transaction);

            MoneyFlowErrorLog errorLog = eventErrorLogFactory.createMoneyFlowErrorLog(
                    transaction, e
            );
            moneyFlowErrorsRepository.save(errorLog);
        } finally {
            return transaction;
        }
    }

    @Transactional
    public DepositTransaction depositFunds(UUID initiatorId, UUID toAccountId,
                                           BigDecimal amount, String description,
                                           String depositMethod) {
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        DepositTransaction transaction = transactionFactory.createDeposit(
                initiatorId, toAccountId, amount, description, depositMethod
        );

        try {
            validateAccountActive(toAccount);
            toAccount.deposit(amount);

            transaction.setCompleted();

            accountRepository.save(toAccount);
            transactionRepository.save(transaction);
        } catch (Exception e) {
            transaction.setFailed(e);
            transactionRepository.save(transaction);

            MoneyFlowErrorLog errorLog = eventErrorLogFactory.createMoneyFlowErrorLog(
                    transaction, e
            );
            moneyFlowErrorsRepository.save(errorLog);
        } finally {
            return transaction;
        }
    }

    @Transactional
    public WithdrawalTransaction withdrawFunds(UUID initiatorId, UUID fromAccountId,
                                               BigDecimal amount, String description,
                                               String withdrawalLocation) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));

        WithdrawalTransaction transaction = transactionFactory.createWithdrawal(
                initiatorId, fromAccountId, amount, description, withdrawalLocation
        );

        try {
            validateAccountActive(fromAccount);
            validateForSufficientFunds(fromAccount, amount);

            fromAccount.withdraw(amount);
            transaction.setCompleted();

            accountRepository.save(fromAccount);
            transactionRepository.save(transaction);
        } catch (Exception e) {
            transaction.setFailed(e);
            transactionRepository.save(transaction);

            MoneyFlowErrorLog errorLog = eventErrorLogFactory.createMoneyFlowErrorLog(
                    transaction, e
            );
            moneyFlowErrorsRepository.save(errorLog);
        } finally {
            return transaction;
        }
    }

    @Transactional
    public AccountFreezeEvent freezeAccount(UUID initiatorId, UUID accountToFreeze,
                                            String description) {
        Account account = accountRepository.findById(accountToFreeze)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found: " + accountToFreeze));

        AccountFreezeEvent event = lifecycleEventsFactory.createAccountFreezeEvent(
                accountToFreeze,
                initiatorId,
                description,
                ActionType.FREEZE
        );

        try {
            if (account.isFrozen()) {
                throw new IllegalStateException("Account is already frozen: " + accountToFreeze);
            }

            account.freeze();
            event.setCompleted();

            accountRepository.save(account);
            accountFreezeActionsRepository.save(event);
        } catch (Exception e) {
            event.setFailed(e);
            accountFreezeActionsRepository.save(event);

            FreezeErrorLog errorLog = eventErrorLogFactory.createFreezeErrorLog(
                    event, e
            );
            lifecycleErrorsRepository.save(errorLog);
        } finally {
            return event;
        }
    }

    @Transactional
    public AccountFreezeEvent unfreezeAccount(UUID initiatorId, UUID accountToUnfreeze, String description) {
        Account account = accountRepository.findById(accountToUnfreeze)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found: " + accountToUnfreeze));

        AccountFreezeEvent event = lifecycleEventsFactory.createAccountFreezeEvent(
                accountToUnfreeze,
                initiatorId,
                description,
                ActionType.UNFREEZE
        );
        try {
            if (account.isActive()) {
                throw new IllegalStateException("Account is already active: " + accountToUnfreeze);
            }
            account.activate();
            event.setCompleted();

            accountRepository.save(account);
            accountFreezeActionsRepository.save(event);
        } catch (Exception e) {
            event.setFailed(e);
            accountFreezeActionsRepository.save(event);

            FreezeErrorLog errorLog = eventErrorLogFactory.createFreezeErrorLog(
                    event, e
            );
            lifecycleErrorsRepository.save(errorLog);
        } finally {
            return event;
        }
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

