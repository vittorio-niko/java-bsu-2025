package bsu.famcs.nikonchik.lab2.backend.repositories;

import bsu.famcs.nikonchik.lab2.backend.entities.events.AccountFreezeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface AccountFreezeActionsRepository extends
        JpaRepository<AccountFreezeEvent, UUID> {
    List<AccountFreezeEvent> findByAccountId(UUID accountId);
}
