package com.kakaopay.test.bankingsystem.domain.exception;

public class LookupRuleViolationException extends IllegalArgumentException {

    public LookupRuleViolationException() {
    }

    public LookupRuleViolationException(String s) {
        super(s);
    }

    public LookupRuleViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LookupRuleViolationException(Throwable cause) {
        super(cause);
    }
}
