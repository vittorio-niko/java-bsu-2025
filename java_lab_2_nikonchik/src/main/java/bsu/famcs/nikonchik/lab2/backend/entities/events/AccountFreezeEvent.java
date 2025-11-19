package bsu.famcs.nikonchik.lab2.backend.entities.events;

import jdk.jfr.EventType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "account_freeze_events")
public class AccountFreezeEvent implements Serializable, Comparable<AccountFreezeEvent> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "event_id", nullable = false, updatable = false, unique = true)
    private final UUID eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, updatable = false, length = 20)
    private ActionType actionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private UUID initiatorId;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name = "description", length = 300)
    private String description;

    protected AccountFreezeEvent() {
        this.eventId = null;
        this.accountId = null;
        this.initiatorId = null;
        this.timestamp = null;
        this.description = null;
    }

    public AccountFreezeEvent(UUID accountId, UUID initiatorId,
                              LocalDateTime timestamp, String description,
                              ActionType actionType) {
        this.eventId = UUID.randomUUID();
        this.accountId = Objects.requireNonNull(accountId,
                "Account cannot be null");
        this.actionType = Objects.requireNonNull(actionType,
                "Event type cannot be null");
        this.initiatorId = Objects.requireNonNull(initiatorId,
                "Actor cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp,
                "Timestamp cannot be null");
        this.description = description;
    }

    public UUID getEventId() { return eventId; }
    public UUID getAccount() { return accountId; }
    public UUID getPerformedBy() { return initiatorId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    public enum ActionType {
        FREEZE,
        UNFREEZE
    }

    @Override
    public int compareTo(AccountFreezeEvent other) {
        return this.timestamp.compareTo(other.timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountFreezeEvent that = (AccountFreezeEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "AccountFreezeEvent{" +
                "eventId=" + eventId +
                ", accountId=" + accountId +
                ", eventType=" + actionType +
                ", performedBy=" + initiatorId +
                ", createdAt=" + timestamp +
                ", description='" + description + '\'' +
                '}';
    }
}