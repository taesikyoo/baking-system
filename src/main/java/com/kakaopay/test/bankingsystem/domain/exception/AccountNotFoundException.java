package com.kakaopay.test.bankingsystem.domain.exception;

import javax.persistence.EntityNotFoundException;

public class AccountNotFoundException extends EntityNotFoundException {

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(String message) {
        super("존재하지 않는 계좌입니다." + message);
    }
}
