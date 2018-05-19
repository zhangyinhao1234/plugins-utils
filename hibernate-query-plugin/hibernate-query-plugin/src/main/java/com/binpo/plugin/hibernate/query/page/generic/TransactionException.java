package com.binpo.plugin.hibernate.query.page.generic;


public class TransactionException extends RuntimeException {
    public TransactionException(Throwable e) {
        super(e);
    }
}