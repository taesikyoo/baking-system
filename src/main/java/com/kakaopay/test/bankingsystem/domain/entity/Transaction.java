package com.kakaopay.test.bankingsystem.domain.entity;

import com.kakaopay.test.bankingsystem.domain.exception.TransactionStatusChangeValidationException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @ManyToOne
    private Account account;
    private long amount;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Transaction(TransactionStatus status, Account account, long amount, Long userId, LocalDateTime createdAt) {
        this.status = status;
        this.account = account;
        this.amount = amount;
        this.userId = userId;
        this.createdAt = createdAt;
        this.modifiedAt = createdAt;
    }

    public static Transaction depositCompleted(Account account, long amount, Long userId, LocalDateTime createdAt) {
        return Transaction.builder()
                .status(TransactionStatus.DEPOSIT_COMPLETED)
                .account(account)
                .amount(amount)
                .userId(userId)
                .createdAt(createdAt)
                .build();
    }

    public static Transaction withdrawStandby(Account account, long amount, Long userId, LocalDateTime createdAt) {
        return Transaction.builder()
                .status(TransactionStatus.WITHDRAW_STANDBY)
                .account(account)
                .amount(amount)
                .userId(userId)
                .createdAt(createdAt)
                .build();
    }

    public static Transaction withdrawCompleted(Account account, long amount, Long userId, LocalDateTime createdAt) {
        return Transaction.builder()
                .status(TransactionStatus.WITHDRAW_COMPLETED)
                .account(account)
                .amount(amount)
                .userId(userId)
                .createdAt(createdAt)
                .build();
    }

    public void toNextStatus(TransactionStatus nextStatus, Long userId) {
        if (!status.getPossibleNextStatus().contains(nextStatus)) {
            throw new TransactionStatusChangeValidationException("유효하지 않은 트랜잭션 상태 변화입니다.");
        }
        this.status = nextStatus;
        this.userId = userId;
        this.modifiedAt = LocalDateTime.now();
    }
}
