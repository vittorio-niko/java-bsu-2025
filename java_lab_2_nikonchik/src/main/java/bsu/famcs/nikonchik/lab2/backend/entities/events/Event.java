package bsu.famcs.nikonchik.lab2.backend.entities.events;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

//Унаследовать от этого класса все events!!!
@MappedSuperclass
public abstract class Event implements Serializable, Comparable<Event> {
    private static final long serialVersionUID = 42L;

    @Id
    @Column(name = "event_id", nullable = false, updatable = false, unique = true)
    protected final UUID eventId;

    @Column(name = "initiator_id", nullable = false, updatable = false)
    protected final UUID initiatorId;

    @Column(name = "timestamp", nullable = false, updatable = false)
    protected final LocalDateTime timestamp;

    @Column(name = "description", nullable = false, length = 30)
    protected final String description;

    protected Event() {
        this.eventId = null;
        this.initiatorId = null;
        this.timestamp = null;
        this.description = null;
    }

    protected Event(UUID eventId, UUID initiatorId,
                    LocalDateTime timestamp, String description) {
        this.eventId = Objects.requireNonNull(eventId,
                "Event id cannot be null");
        this.initiatorId = Objects.requireNonNull(initiatorId,
                "Initiator id cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp,
                "Timestamp cannot be null");
        this.description = Objects.requireNonNull(description,
                "Description cannot be null");
    }

    @Override
    public int compareTo(Event other) {
        return this.timestamp.compareTo(other.timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event that = (Event) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    public UUID getEventId() { return eventId; }
    public UUID getInitiatorId() { return initiatorId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
}
