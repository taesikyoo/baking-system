package com.kakaopay.test.bankingsystem.domain.entity;

import com.kakaopay.test.bankingsystem.domain.exception.TransactionStatusChangeValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionTest {

    @Test
    void success() {
        Transaction transaction = Transaction.withdrawStandby(null, 100000, null, LocalDateTime.now());
        transaction.toNextStatus(TransactionStatus.WITHDRAW_COMPLETED, 3L);
        assertThat(transaction.getUserId()).isEqualTo(3L);
        assertThat(transaction.getStatus()).isEqualByComparingTo(TransactionStatus.WITHDRAW_COMPLETED);
    }

    @Test
    void invalidStatusChange() {
        Transaction transaction = Transaction.depositCompleted(null, 100000, null, LocalDateTime.now());
        assertThatThrownBy(() -> transaction.toNextStatus(TransactionStatus.WITHDRAW_COMPLETED, 3L))
                .isInstanceOf(TransactionStatusChangeValidationException.class);
    }
}