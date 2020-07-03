package com.kakaopay.test.bankingsystem.domain.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AccountCreateRequest {

    private String token;
    private Long ownerId;
    private String roomId;
    private LocalDateTime createdAt;
    private LocalDateTime withdrawExpiredAt;
    private LocalDateTime lookupExpiredAt;

    @Builder
    public AccountCreateRequest(String token, Long ownerId, String roomId, LocalDateTime createdAt, LocalDateTime withdrawExpiredAt, LocalDateTime lookupExpiredAt) {
        this.token = token;
        this.ownerId = ownerId;
        this.roomId = roomId;
        this.createdAt = createdAt;
        this.withdrawExpiredAt = withdrawExpiredAt;
        this.lookupExpiredAt = lookupExpiredAt;
    }
}
