package com.kakaopay.test.bankingsystem.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LuckyAccountCreateResponse {

    private String token;
    private Long ownerId;
    private LocalDateTime createdAt;

    @Builder
    public LuckyAccountCreateResponse(String token, Long ownerId, LocalDateTime createdAt) {
        this.token = token;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
    }
}
