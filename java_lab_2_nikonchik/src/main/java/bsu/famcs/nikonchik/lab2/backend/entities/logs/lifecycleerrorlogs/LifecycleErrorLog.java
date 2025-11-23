package bsu.famcs.nikonchik.lab2.backend.entities.logs.lifecycleerrorlogs;

import javax.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Objects;

import bsu.famcs.nikonchik.lab2.backend.entities.logs.EventErrorLog;

@Entity
@Table(name = "lifecycle_errors")
public class LifecycleErrorLog extends EventErrorLog {
    public enum ActionType {
        FREEZE, UNFREEZE, CREATE
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20, updatable = false)
    private final ActionType actionType;

    public LifecycleErrorLog() {
        super();
        this.actionType = null;
    }

    public LifecycleErrorLog(UUID logId, UUID eventId,
                             UUID initiatorId, LocalDateTime timestamp,
                             String errorCode, String errorMessage,
                             String stacktrace, ActionType actionType) {
        super(logId, eventId, initiatorId, timestamp,
                errorCode, errorMessage, stacktrace);
        this.actionType = Objects.requireNonNull(actionType,
                "Action type cannot be null");
    }

    public ActionType getActionType() { return actionType; }
}
