package com.binpo.plugin.hibernate.query.page.generic;


public abstract class Transaction implements Command {
	private TransactionException e = null;

	public TransactionException getTransctionException() {
		return e;
	}

	public void setTransactionException(TransactionException e) {
		this.e = e;
	}
}
