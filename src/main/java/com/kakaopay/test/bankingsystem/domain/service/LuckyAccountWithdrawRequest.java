package com.kakaopay.test.bankingsystem.domain.service;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LuckyAccountWithdrawRequest {

    private String token;
    private String roomId;
    private Long userId;
    private LocalDateTime requestedAt;

    @Builder
    public LuckyAccountWithdrawRequest(String token, String roomId, Long userId, LocalDateTime requestedAt) {
        this.token = token;
        this.roomId = roomId;
        this.userId = userId;
        this.requestedAt = requestedAt;
    }
}
