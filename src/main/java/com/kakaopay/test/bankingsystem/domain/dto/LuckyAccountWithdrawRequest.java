package com.kakaopay.test.bankingsystem.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LuckyAccountWithdrawRequest {

    private final String token;
    private final String roomId;
    private final Long userId;
    private final LocalDateTime requestAt;

    @Builder
    public LuckyAccountWithdrawRequest(String token, String roomId, Long userId, LocalDateTime requestAt) {
        this.token = token;
        this.roomId = roomId;
        this.userId = userId;
        this.requestAt = requestAt;
    }
}
