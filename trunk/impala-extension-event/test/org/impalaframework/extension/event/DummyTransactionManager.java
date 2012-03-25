package org.impalaframework.extension.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

public class DummyTransactionManager implements PlatformTransactionManager {

    protected final Log log = LogFactory.getLog(getClass());

    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        log.debug("getTransaction() called with TransactionDefinition:[" + definition.toString() + "], returning new SimpleTransactionStatus().");
        return new SimpleTransactionStatus();
    }

    public void commit(TransactionStatus status) throws TransactionException {
        log.debug("commit() called with TransactionStatus:[" + status.toString() + "].");
    }

    public void rollback(TransactionStatus status) throws TransactionException {
        log.debug("rollback() called with TransactionStatus:[" + status.toString() + "].");
    }

}
