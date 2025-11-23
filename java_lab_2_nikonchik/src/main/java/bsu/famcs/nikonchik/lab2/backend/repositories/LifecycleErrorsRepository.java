package bsu.famcs.nikonchik.lab2.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import bsu.famcs.nikonchik.lab2.backend.entities.logs.lifecycleerrorlogs.LifecycleErrorLog;
import bsu.famcs.nikonchik.lab2.backend.entities.logs.lifecycleerrorlogs.LifecycleErrorLog.ActionType;

public interface LifecycleErrorsRepository extends
        JpaRepository<LifecycleErrorLog, UUID> {
    Optional<LifecycleErrorLog> findByAccountId(UUID accountId);
    Optional<LifecycleErrorLog> findByEventId(UUID eventId);
    List<LifecycleErrorLog> findByActionType(ActionType actionType);
}
