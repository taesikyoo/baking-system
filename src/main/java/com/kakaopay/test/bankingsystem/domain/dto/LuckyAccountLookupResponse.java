package com.kakaopay.test.bankingsystem.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class LuckyAccountLookupResponse {

    private LocalDateTime createdAt;
    private long depositedAmount;
    private long withdrawAmount;
    private List<TransactionDTO> withdrawCompleted;

    @Builder
    public LuckyAccountLookupResponse(LocalDateTime createdAt, long depositedAmount, long withdrawAmount, List<TransactionDTO> withdrawCompleted) {
        this.createdAt = createdAt;
        this.depositedAmount = depositedAmount;
        this.withdrawAmount = withdrawAmount;
        this.withdrawCompleted = withdrawCompleted;
    }
}
