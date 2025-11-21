package bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING, length = 15)
public abstract class Transaction implements Serializable, Comparable<Transaction> {
    private static final long serialVersionUID = 7L;

    @Id
    @Column(name = "transaction_id", nullable = false, updatable = false, unique = true)
    protected final UUID id;

    @Column(name = "amount", nullable = false, updatable = false, precision = 15, scale = 2)
    protected final BigDecimal amount;

    @Column(name = "timestamp", nullable = false, updatable = false)
    protected final LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    protected TransactionStatus status;

    @Column(name = "description", nullable = false, length = 30)
    protected final String description;

    protected Transaction() {
        this.id = null;
        this.amount = null;
        this.timestamp = null;
        this.status = null;
        this.description = null;
    }

    protected Transaction(UUID id, BigDecimal amount, LocalDateTime timestamp,
                          TransactionStatus status, String description) {
        this.id = Objects.requireNonNull(id, "Transaction ID cannot be null");
        this.amount = Objects.requireNonNull(amount, "Transaction amount cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "Transaction timestamp cannot be null");
        this.status = Objects.requireNonNull(status, "Transaction status cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, UNDONE, FAILED
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
        return String.format("Transaction{id='%s', amount=%s, timestamp=%s, status='%s'}",
                id, amount, timestamp, status);
    }

    public UUID getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionStatus getStatus() { return status; }
    public String getDescription() { return description; }

    public void setStatus(TransactionStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}
