package bsu.famcs.nikonchik.lab2.backend.entities.logs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Objects;

//@Entity
//@Table(name = "transaction_failure_details")
//public class MoneyFlowErrorLog {
//    @Id
//    @Column(name = "transaction_id", nullable = false)
//    private UUID transactionId;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id",
//            insertable = false, updatable = false)
//    private UUID transaction;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "failure_category", nullable = false, length = 30)
//    private FailureCategory failureCategory;
//
//    @Column(name = "error_code", nullable = false, length = 50)
//    private String errorCode;
//
//    @Column(name = "error_message", length = 1000)
//    private String errorMessage;
//
//    @Column(name = "stacktrace", columnDefinition = "TEXT")
//    private String stacktrace;
//
//    @Column(name = "failed_at", nullable = false)
//    private LocalDateTime failedAt;
//
//
//    public MoneyFlowErrorLog() {}
//
//    public MoneyFlowErrorLog(UUID transactionId, FailureCategory failureCategory,
//                                     String errorCode, String errorMessage, String stacktrace) {
//        this.transactionId = transactionId;
//        this.failureCategory = failureCategory;
//        this.errorCode = errorCode;
//        this.errorMessage = errorMessage;
//        this.stacktrace = stacktrace;
//        this.failedAt = LocalDateTime.now();
//    }
//
//    // Enum для категоризации ошибок
//    public enum FailureCategory {
//        VALIDATION,           // Ошибки валидации (недостаточно средств и т.д.)
//        BUSINESS_RULE,        // Нарушение бизнес-правил
//        SECURITY,             // Ошибки безопасности (доступ запрещен)
//        TECHNICAL,            // Технические ошибки (БД, сеть и т.д.)
//        INTEGRATION,          // Ошибки интеграции с внешними системами
//        TIMEOUT,              // Таймауты операций
//        CONCURRENCY,          // Проблемы параллелизма (deadlock)
//        UNKNOWN               // Неизвестные ошибки
//    }
//
//    // Геттеры и сеттеры
//    public UUID getTransactionId() { return transactionId; }
//    public void setTransactionId(UUID transactionId) { this.transactionId = transactionId; }
//
//    public Transaction getTransaction() { return transaction; }
//    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
//
//    public FailureCategory getFailureCategory() { return failureCategory; }
//    public void setFailureCategory(FailureCategory failureCategory) { this.failureCategory = failureCategory; }
//
//    public String getErrorCode() { return errorCode; }
//    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
//
//    public String getErrorMessage() { return errorMessage; }
//    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
//
//    public String getStacktrace() { return stacktrace; }
//    public void setStacktrace(String stacktrace) { this.stacktrace = stacktrace; }
//
//    public LocalDateTime getFailedAt() { return failedAt; }
//    public void setFailedAt(LocalDateTime failedAt) { this.failedAt = failedAt; }
//
//    public String getRecoverySuggestion() { return recoverySuggestion; }
//    public void setRecoverySuggestion(String recoverySuggestion) { this.recoverySuggestion = recoverySuggestion; }
//
//    public String getUserContext() { return userContext; }
//    public void setUserContext(String userContext) { this.userContext = userContext; }
//
//    public String getSystemContext() { return systemContext; }
//    public void setSystemContext(String systemContext) { this.systemContext = systemContext; }
//
//    public Boolean getRetryPossible() { return retryPossible; }
//    public void setRetryPossible(Boolean retryPossible) { this.retryPossible = retryPossible; }
//
//    public Boolean getAutomaticRetryAttempted() { return automaticRetryAttempted; }
//    public void setAutomaticRetryAttempted(Boolean automaticRetryAttempted) { this.automaticRetryAttempted = automaticRetryAttempted; }
//}
