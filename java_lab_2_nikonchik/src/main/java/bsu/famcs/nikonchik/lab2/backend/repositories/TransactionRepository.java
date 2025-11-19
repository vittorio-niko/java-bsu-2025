package bsu.famcs.nikonchik.lab2.backend.repositories;

import bsu.famcs.nikonchik.lab2.backend.entities.events.Transaction;
import bsu.famcs.nikonchik.lab2.backend.entities.events.Transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface TransactionRepository extends
        JpaRepository<Transaction, UUID> {
    List<Transaction> findByTransactionType(TransactionType type);
}
