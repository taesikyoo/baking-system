package com.kakaopay.test.bankingsystem.domain.service;

import com.kakaopay.test.bankingsystem.domain.dto.AccountCreateRequest;
import com.kakaopay.test.bankingsystem.domain.dto.LuckyAccountCreateRequest;
import com.kakaopay.test.bankingsystem.domain.dto.LuckyAccountWithdrawRequest;
import com.kakaopay.test.bankingsystem.domain.entity.Account;
import com.kakaopay.test.bankingsystem.domain.entity.Transaction;
import com.kakaopay.test.bankingsystem.domain.entity.TransactionStatus;
import com.kakaopay.test.bankingsystem.domain.exception.WithdrawFailureException;
import com.kakaopay.test.bankingsystem.domain.exception.WithdrawRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LuckyAccountService {

    public static final int LUCKY_ACCOUNT_WITHDRAW_EXPIRED_MINUTES = 10;
    public static final int LUCKY_ACCOUNT_WITHDRAW_EXPIRED_DAYS = 7;

    private final AccountService accountService;
    private final TransactionService transactionService;

    @Transactional
    public Account create(LuckyAccountCreateRequest request) {
        Long userId = request.getUserId();
        long amount = request.getAmount();
        int withdrawLimit = request.getWithdrawLimit();
        LocalDateTime requestedAt = LocalDateTime.now();

        AccountCreateRequest accountCreatRequest = AccountCreateRequest.builder()
                // TODO: 2020-07-01 token 만들기
                .token("abc")
                .ownerId(userId)
                .roomId(request.getRoomId())
                .createdAt(requestedAt)
                .withdrawExpiredAt(requestedAt.plusMinutes(LUCKY_ACCOUNT_WITHDRAW_EXPIRED_MINUTES))
                .lookupExpiredAt(requestedAt.plusDays(LUCKY_ACCOUNT_WITHDRAW_EXPIRED_DAYS))
                .build();

        Account account = accountService.create(accountCreatRequest);
        transactionService.deposit(account, request.getAmount(), userId);
        // TODO: 2020-07-01 분배규칙 만들기
        for (int i = 0; i < withdrawLimit; i++) {
            transactionService.withdrawStandby(account, amount / withdrawLimit);
        }

        return account;
    }

    @Transactional
    public void withdraw(LuckyAccountWithdrawRequest request) {
        Account account = accountService.findByToken(request.getToken());
        Long userId = request.getUserId();
        if (!account.getRoomId().equals(request.getRoomId())) {
            throw new WithdrawRuleViolationException("일치하지 않는 방 번호입니다.");
        }
        if (request.getRequestedAt().isAfter(account.getWithdrawExpiredAt())) {
            throw new WithdrawRuleViolationException("뿌리기가 만료되었습니다.");
        }
        if (account.getOwnerId().equals(userId)) {
            throw new WithdrawRuleViolationException("자신이 생성한 뿌리기는 받을 수 없습니다.");
        }

        List<Transaction> transactions = transactionService.findByAccount(account);
        if (hasTransactionAlready(request, transactions)) {
            throw new WithdrawRuleViolationException("이미 받은 뿌리기는 중복으로 다시 받을 수 없습니다.");
        }

        Transaction withdrawStandby = transactions.stream()
                .filter(transaction -> TransactionStatus.WITHDRAW_STANDBY == transaction.getStatus())
                .findFirst()
                .orElseThrow(() -> new WithdrawFailureException("더 이상 남은 뿌리기가 없습니다."));

        withdrawStandby.toNextStatus(TransactionStatus.WITHDRAW_COMPLETED, userId);
    }

    private boolean hasTransactionAlready(LuckyAccountWithdrawRequest request, List<Transaction> transactions) {
        return transactions.stream().anyMatch(transaction -> request.getUserId().equals(transaction.getUserId()));
    }
}
