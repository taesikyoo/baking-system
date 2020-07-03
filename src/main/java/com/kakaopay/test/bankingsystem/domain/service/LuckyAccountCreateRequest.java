package com.kakaopay.test.bankingsystem.domain.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LuckyAccountCreateRequest {

    private String token;
    private Long userId;
    private String roomId;
    private long amount;
    private int withdrawLimit;
    private LocalDateTime requestedAT;

    @Builder
    public LuckyAccountCreateRequest(String token, Long userId, String roomId, long amount, int withdrawLimit, LocalDateTime requestedAT) {
        this.token = token;
        this.userId = userId;
        this.roomId = roomId;
        this.amount = amount;
        this.withdrawLimit = withdrawLimit;
        this.requestedAT = requestedAT;
    }
}
