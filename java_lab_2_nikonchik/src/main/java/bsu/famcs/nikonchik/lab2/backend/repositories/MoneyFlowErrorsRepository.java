package bsu.famcs.nikonchik.lab2.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import bsu.famcs.nikonchik.lab2.backend.entities.logs.moneyflowerrorlogs.MoneyFlowErrorLog;
import bsu.famcs.nikonchik.lab2.backend.entities.logs.moneyflowerrorlogs.MoneyFlowErrorLog.TransactionType;

public interface MoneyFlowErrorsRepository extends
        JpaRepository<MoneyFlowErrorLog, UUID> {
    Optional<MoneyFlowErrorLog> findByAccountId(UUID accountId);
    Optional<MoneyFlowErrorLog> findByEventId(UUID eventId);
    List<MoneyFlowErrorLog> findByTransactionType(TransactionType transactionType);
}
