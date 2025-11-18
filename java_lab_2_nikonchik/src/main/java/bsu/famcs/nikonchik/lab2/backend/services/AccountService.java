package bsu.famcs.nikonchik.lab2.backend.services;

import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bsu.famcs.nikonchik.lab2.backend.entities.actions.Transaction;
import bsu.famcs.nikonchik.lab2.backend.entities.actions.Transaction.*;
import bsu.famcs.nikonchik.lab2.backend.entities.products.Account;
import bsu.famcs.nikonchik.lab2.backend.exceptions.AccountExceptions.*;

import bsu.famcs.nikonchik.lab2.backend.repositories.AccountRepository;
import bsu.famcs.nikonchik.lab2.backend.repositories.TransactionRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
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
        );

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        transaction.setStatus(TransactionStatus.COMPLETED);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        transactionRepository.save(transaction);

        return transaction;
    }
}

