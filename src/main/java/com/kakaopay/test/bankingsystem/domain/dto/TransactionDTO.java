package com.kakaopay.test.bankingsystem.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransactionDTO {

    private long amount;
    private Long userId;

    @Builder
    public TransactionDTO(long amount, Long userId) {
        this.amount = amount;
        this.userId = userId;
    }
}
