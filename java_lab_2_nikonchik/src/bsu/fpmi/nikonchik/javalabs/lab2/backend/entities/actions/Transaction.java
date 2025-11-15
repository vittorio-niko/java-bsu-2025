package bsu.fpmi.nikonchik.javalabs.lab2.backend.entities.actions;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction implements Serializable, Comparable<Transaction> {
    private static final long serialVersionUID = 4L;

    @Id
    @Column(name = "transaction_id", nullable = false, updatable = false, unique = true)
    private final UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, updatable = false, length = 20)
    private final TransactionType type;

    @Column(name = "amount", nullable = false, updatable = false, precision = 15, scale = 2)
    private final BigDecimal amount;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private final LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TransactionStatus status;

    @Column(name = "from_account_id", updatable = false)
    private final UUID fromAccount;

    @Column(name = "to_account_id", updatable = false)
    private final UUID toAccount;

    protected Transaction() {
        this.id = null;
        this.type = null;
        this.amount = null;
        this.timestamp = null;
        this.status = null;
        this.fromAccount = null;
        this.toAccount = null;
    }

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

    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER
    }

    public enum TransactionStatus {
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

    public UUID getId() { return id; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionStatus getStatus() { return status; }
    public UUID getFromAccount() { return fromAccount; }
    public UUID getToAccount() { return toAccount; }

    public void setStatus(TransactionStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}

