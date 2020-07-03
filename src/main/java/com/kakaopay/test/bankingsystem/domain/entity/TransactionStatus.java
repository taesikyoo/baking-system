package com.kakaopay.test.bankingsystem.domain.entity;

import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum TransactionStatus {

    DEPOSIT_COMPLETED,
    WITHDRAW_STANDBY,
    WITHDRAW_COMPLETED,
    WITHDRAW_TIMEOUT_EXPIRED;

    private EnumSet<TransactionStatus> possibleNextStatus;

    static {
        DEPOSIT_COMPLETED.possibleNextStatus = EnumSet.noneOf(TransactionStatus.class);
        WITHDRAW_STANDBY.possibleNextStatus = EnumSet.of(WITHDRAW_COMPLETED, WITHDRAW_TIMEOUT_EXPIRED);
        WITHDRAW_COMPLETED.possibleNextStatus = EnumSet.noneOf(TransactionStatus.class);
        WITHDRAW_TIMEOUT_EXPIRED.possibleNextStatus = EnumSet.noneOf(TransactionStatus.class);
    }
}
