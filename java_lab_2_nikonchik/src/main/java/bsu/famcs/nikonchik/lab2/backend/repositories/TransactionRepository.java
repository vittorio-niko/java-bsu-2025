package bsu.famcs.nikonchik.lab2.backend.repositories;

import bsu.famcs.nikonchik.lab2.backend.entities.actions.Transaction;
import bsu.famcs.nikonchik.lab2.backend.entities.actions.Transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByTransactionType(TransactionType type);
}
