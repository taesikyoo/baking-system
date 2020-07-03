package com.kakaopay.test.bankingsystem.domain.dto;

import com.kakaopay.test.bankingsystem.domain.entity.Account;
import com.kakaopay.test.bankingsystem.domain.entity.TransactionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LuckyAccountWithdrawResponse {

    private Long id;
    private TransactionStatus status;
    private long amount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public LuckyAccountWithdrawResponse(Long id, TransactionStatus status, long amount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
