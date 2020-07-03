package com.kakaopay.test.bankingsystem.domain.service;

import com.kakaopay.test.bankingsystem.domain.dto.AccountCreateRequest;
import com.kakaopay.test.bankingsystem.domain.entity.Account;
import com.kakaopay.test.bankingsystem.domain.exception.AccountNotFoundException;
import com.kakaopay.test.bankingsystem.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account create(AccountCreateRequest request) {
        Account account = Account.builder()
                .token(request.getToken())
                .ownerId(request.getOwnerId())
                .roomId(request.getRoomId())
                .createdAt(request.getCreatedAt())
                .withdrawExpiredAt(request.getWithdrawExpiredAt())
                .lookupExpiredAt(request.getLookupExpiredAt())
                .build();
        return accountRepository.save(account);
    }

    public Account findByToken(String token) {
        return accountRepository.findByToken(token)
                .orElseThrow(() -> new AccountNotFoundException("토큰=" + token));
    }
}
