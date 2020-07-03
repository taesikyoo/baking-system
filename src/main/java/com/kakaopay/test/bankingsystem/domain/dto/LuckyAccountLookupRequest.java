package com.kakaopay.test.bankingsystem.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LuckyAccountLookupRequest {

    private String token;
    private Long userId;
    private LocalDateTime requestedAt;

    @Builder
    public LuckyAccountLookupRequest(String token, Long userId, LocalDateTime requestedAt) {
        this.token = token;
        this.userId = userId;
        this.requestedAt = requestedAt;
    }
}
