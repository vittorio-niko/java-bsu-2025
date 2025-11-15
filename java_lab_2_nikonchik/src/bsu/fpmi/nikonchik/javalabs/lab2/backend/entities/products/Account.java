package bsu.fpmi.nikonchik.javalabs.lab2.backend.entities.products;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account extends Product implements Comparable<Account>, Serializable {
    private static final long serialVersionUID = 2L;

    @Column(name = "account_number", nullable = false, updatable = false, unique = true, length = 20)
    private final String accountNumber;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, updatable = false, length = 3)
    private final Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatus status;

    @Column(name = "account_name", length = 100)
    private String accountName;

    protected Account() {
        super();
        this.accountNumber = null;
        this.balance = null;
        this.currency = null;
        this.status = null;
        this.accountName = null;
    }

    public Account(UUID productId, UUID ownerId, LocalDateTime createdDate,
                   String accountNumber, BigDecimal initialBalance,
                   Currency currency) {
        super(productId, ownerId, createdDate);
        this.accountNumber = Objects.requireNonNull(accountNumber, "Account number cannot be null");
        this.balance = Objects.requireNonNull(initialBalance, "Initial balance cannot be null");
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
        this.status = AccountStatus.ACTIVE;
        this.accountName = accountName;
    }

    public enum Currency { USD, EUR, GBP, JPY, BYN }
    public enum AccountStatus { ACTIVE, BLOCKED, CLOSED, SUSPENDED }

    @Override
    public int compareTo(Account other) {
        return this.balance.compareTo(other.balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accountNumber);
    }

    @Override
    public String toString() {
        return String.format("Account{id='%s', number='%s', balance=%s %s, type=%s, status=%s, name='%s'}",
                productId, accountNumber, balance, currency, status,
                accountName != null ? accountName : "N/A");
    }

    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public Currency getCurrency() { return currency; }
    public AccountStatus getStatus() { return status; }
    public String getAccountName() { return accountName; }

    public void setBalance(BigDecimal balance) {
        this.balance = Objects.requireNonNull(balance, "Balance cannot be null");
    }

    public void setStatus(AccountStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
