package bsu.fpmi.nikonchik.javalabs.lab2.backend.entities.actions;

import bsu.fpmi.nikonchik.javalabs.lab2.backend.entities.actors.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Objects;

public class Transaction implements Serializable, Comparable<Transaction> {
    private final UUID id;
    private final TransactionType type;
    private final BigDecimal amount;
    private LocalDateTime timestamp;
    private TransactionStatus status;
    private final UUID fromAccount;
    private final UUID toAccount;

    public Transaction(UUID id, TransactionType type,
                       BigDecimal amount, LocalDateTime timestamp,
                       TransactionStatus status, UUID fromAccount, UUID toAccount) {
        this.id = Objects.requireNonNull(id);
        this.type = Objects.requireNonNull(type);
        this.amount = Objects.requireNonNull(amount);
        this.timestamp = Objects.requireNonNull(timestamp);
        this.status = Objects.requireNonNull(status);
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER
    }

    enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED
    }

    @Override
    public int compareTo(Transaction other) {
        return this.timestamp.compareTo(other.timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', type='%s', amount=%s, timestamp=%s, " +
                        "status='%s', fromAccount='%s', toAccount='%s'}",
                id, type, amount, timestamp, status,
                fromAccount != null ? fromAccount : "N/A",
                toAccount != null ? toAccount : "N/A");
    }
}
