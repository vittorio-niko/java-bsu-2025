package bsu.famcs.nikonchik.lab2.backend.entities.logs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Objects;

@MappedSuperclass
public abstract class EventErrorLog implements Serializable,
        Comparable<EventErrorLog> {
    @Id
    @Column(name = "log_id", nullable = false, unique = true)
    protected UUID logId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id",
            insertable = false, updatable = false)
    protected UUID eventId;

    @Column(name = "error_code", nullable = false, length = 50)
    protected String errorCode;

    @Column(name = "error_message", nullable = false, length = 600)
    protected String errorMessage;

    @Column(name = "stacktrace", nullable = false, columnDefinition = "TEXT")
    protected String stacktrace;

    protected EventErrorLog() {
        this.logId = null;
        this.eventId = null;
        this.errorCode = null;
        this.errorMessage = null;
        this.stacktrace = null;
    }

    protected EventErrorLog(UUID logId, UUID eventId,
                            String errorCode, String errorMessage,
                            String stacktrace) {
        this.logId = Objects.requireNonNull(logId,
                "Log id cannot be null");
        this.eventId = Objects.requireNonNull(eventId,
                "Event id cannot be null");
        this.errorCode = Objects.requireNonNull(errorCode,
                "Error code cannot be null");
        this.errorMessage = Objects.requireNonNull(errorMessage,
                "Error message cannot be null");
        this.stacktrace = Objects.requireNonNull(stacktrace,
                "Stack trace cannot be null");
    }

    public UUID getLogId() { return logId; }
    public UUID getEventId() { return eventId; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public String getStacktrace() { return stacktrace; }
}
