package bsu.famcs.nikonchik.lab2.backend.factories;

import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.io.StringWriter;
import java.io.PrintWriter;

import bsu.famcs.nikonchik.lab2.backend.entities.logs.lifecycleerrorlogs.LifecycleErrorLog;
import bsu.famcs.nikonchik.lab2.backend.entities.logs.moneyflowerrorlogs.MoneyFlowErrorLog;
import bsu.famcs.nikonchik.lab2.backend.entities.logs.moneyflowerrorlogs.MoneyFlowErrorLog.TransactionType;
import bsu.famcs.nikonchik.lab2.backend.entities.logs.lifecycleerrorlogs.LifecycleErrorLog.ActionType;
import org.springframework.stereotype.Component;

@Component
public class EventErrorLogFactory {
    public MoneyFlowErrorLog createMoneyFlowErrorLog(UUID eventId, UUID initiatorId,
                                                     Exception e, BigDecimal amount,
                                                     TransactionType transactionType) {
        return new MoneyFlowErrorLog(UUID.randomUUID(), eventId, initiatorId, LocalDateTime.now(),
                getErrorCode(e), e.getMessage(), getStackTrace(e), amount, transactionType);
    }

    public LifecycleErrorLog createLifeCycleErrorLog(UUID eventId, UUID initiatorId,
                                                     Exception e, ActionType actionType) {
        return new LifecycleErrorLog(UUID.randomUUID(), eventId, initiatorId, LocalDateTime.now(),
                getErrorCode(e), e.getMessage(), getStackTrace(e), actionType);
    }

    private static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private static String getErrorCode(Exception e) {
        return e.getClass().getSimpleName();
    }
}
