package com.kakaopay.test.bankingsystem.domain.service;

import com.kakaopay.test.bankingsystem.domain.dto.LuckyAccountCreateRequest;
import com.kakaopay.test.bankingsystem.domain.dto.LuckyAccountWithdrawRequest;
import com.kakaopay.test.bankingsystem.domain.entity.Account;
import com.kakaopay.test.bankingsystem.domain.entity.Transaction;
import com.kakaopay.test.bankingsystem.domain.entity.TransactionStatus;
import com.kakaopay.test.bankingsystem.domain.exception.WithdrawFailureException;
import com.kakaopay.test.bankingsystem.domain.exception.WithdrawRuleViolationException;
import com.kakaopay.test.bankingsystem.domain.repository.AccountRepository;
import com.kakaopay.test.bankingsystem.domain.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class LuckyAccountServiceTest {

    @Autowired
    private LuckyAccountService luckyAccountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("뿌리기")
    void create() {
        LocalDateTime now = LocalDateTime.now();
        LuckyAccountCreateRequest luckyAccountCreateRequest = LuckyAccountCreateRequest.builder()
                .token("abc")
                .userId(1L)
                .roomId("room")
                .amount(100000)
                .withdrawLimit(5)
                .requestedAT(now)
                .build();

        luckyAccountService.create(luckyAccountCreateRequest);
        List<Account> accounts = accountRepository.findAll();
        List<Transaction> transactions = transactionRepository.findAll();
        Map<TransactionStatus, List<Transaction>> transactionStatusMap = transactions.stream().collect(Collectors.groupingBy(Transaction::getStatus));

        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getOwnerId()).isEqualTo(1);
        assertThat(accounts.get(0).getWithdrawExpiredAt()).isEqualTo(now.plusMinutes(10));
        assertThat(accounts.get(0).getLookupExpiredAt()).isEqualTo(now.plusDays(7));
        assertThat(transactionStatusMap.get(TransactionStatus.DEPOSIT_COMPLETED)).hasSize(1);
        assertThat(transactionStatusMap.get(TransactionStatus.WITHDRAW_STANDBY)).hasSize(luckyAccountCreateRequest.getWithdrawLimit());
    }

    @Test
    @DisplayName("받기")
    void withdraw() {
        LocalDateTime now = LocalDateTime.now();
        LuckyAccountCreateRequest luckyAccountCreateRequest = LuckyAccountCreateRequest.builder()
                .token("abc")
                .userId(1L)
                .roomId("room")
                .amount(100000)
                .withdrawLimit(5)
                .requestedAT(now)
                .build();
        luckyAccountService.create(luckyAccountCreateRequest);

        LuckyAccountWithdrawRequest luckyAccountWithdrawRequest = LuckyAccountWithdrawRequest.builder()
                .token("abc")
                .roomId("room")
                .userId(2L)
                .requestedAt(now.plusMinutes(1))
                .build();

        luckyAccountService.withdraw(luckyAccountWithdrawRequest);
        List<Transaction> transactions = transactionRepository.findAll();
        Map<TransactionStatus, List<Transaction>> transactionStatusMap = transactions.stream().collect(Collectors.groupingBy(Transaction::getStatus));

        assertThat(transactionStatusMap.get(TransactionStatus.WITHDRAW_STANDBY)).hasSize(luckyAccountCreateRequest.getWithdrawLimit() - 1);
        List<Transaction> completed = transactionStatusMap.get(TransactionStatus.WITHDRAW_COMPLETED);
        assertThat(completed).hasSize(1);
        assertThat(completed.get(0).getUserId()).isEqualTo(2L);
        assertThat(completed.get(0).getAmount()).isEqualTo(20000);
    }

    @Test
    @DisplayName("뿌리기를 생성한 사람이 받기를 시도한다.")
    void withdrawError1() {
        LocalDateTime now = LocalDateTime.now();
        LuckyAccountCreateRequest luckyAccountCreateRequest = LuckyAccountCreateRequest.builder()
                .token("abc")
                .userId(1L)
                .roomId("room")
                .amount(100000)
                .withdrawLimit(5)
                .requestedAT(now)
                .build();
        luckyAccountService.create(luckyAccountCreateRequest);

        LuckyAccountWithdrawRequest luckyAccountWithdrawRequest = LuckyAccountWithdrawRequest.builder()
                .token("abc")
                .roomId("room")
                .userId(1L)
                .requestedAt(now.plusMinutes(1))
                .build();

        assertThatThrownBy(() -> luckyAccountService.withdraw(luckyAccountWithdrawRequest))
                .isInstanceOf(WithdrawRuleViolationException.class)
                .hasMessageContaining("자신이 생성한 뿌리기는 받을 수 없습니다.");
    }

    @Test
    @DisplayName("다른 방에 있는 사람이 받기를 시도한다.")
    void withdrawError2() {
        LocalDateTime now = LocalDateTime.now();
        LuckyAccountCreateRequest luckyAccountCreateRequest = LuckyAccountCreateRequest.builder()
                .token("abc")
                .userId(1L)
                .roomId("room")
                .amount(100000)
                .withdrawLimit(5)
                .requestedAT(now)
                .build();
        luckyAccountService.create(luckyAccountCreateRequest);

        LuckyAccountWithdrawRequest luckyAccountWithdrawRequest = LuckyAccountWithdrawRequest.builder()
                .token("abc")
                .roomId("anotherRoom")
                .userId(2L)
                .requestedAt(now.plusMinutes(1))
                .build();

        assertThatThrownBy(() -> luckyAccountService.withdraw(luckyAccountWithdrawRequest))
                .isInstanceOf(WithdrawRuleViolationException.class)
                .hasMessageContaining("일치하지 않는 방 번호입니다.");
    }

    @Test
    @DisplayName("뿌리기가 만료된 후 받기를 시도한다.")
    void withdrawError3() {
        LocalDateTime now = LocalDateTime.now();
        LuckyAccountCreateRequest luckyAccountCreateRequest = LuckyAccountCreateRequest.builder()
                .token("abc")
                .userId(1L)
                .roomId("room")
                .amount(100000)
                .withdrawLimit(5)
                .requestedAT(now)
                .build();
        luckyAccountService.create(luckyAccountCreateRequest);

        LuckyAccountWithdrawRequest luckyAccountWithdrawRequest = LuckyAccountWithdrawRequest.builder()
                .token("abc")
                .roomId("room")
                .userId(2L)
                .requestedAt(now.plusMinutes(11))
                .build();

        assertThatThrownBy(() -> luckyAccountService.withdraw(luckyAccountWithdrawRequest))
                .isInstanceOf(WithdrawRuleViolationException.class)
                .hasMessageContaining("뿌리기가 만료되었습니다.");
    }

    @Test
    @DisplayName("중복 받기를 시도한다.")
    void withdrawError4() {
        LocalDateTime now = LocalDateTime.now();
        LuckyAccountCreateRequest luckyAccountCreateRequest = LuckyAccountCreateRequest.builder()
                .token("abc")
                .userId(1L)
                .roomId("room")
                .amount(100000)
                .withdrawLimit(5)
                .requestedAT(now)
                .build();
        luckyAccountService.create(luckyAccountCreateRequest);

        LuckyAccountWithdrawRequest luckyAccountWithdrawRequest = LuckyAccountWithdrawRequest.builder()
                .token("abc")
                .roomId("room")
                .userId(2L)
                .requestedAt(now.plusMinutes(1))
                .build();

        luckyAccountService.withdraw(luckyAccountWithdrawRequest);

        assertThatThrownBy(() -> luckyAccountService.withdraw(luckyAccountWithdrawRequest))
                .isInstanceOf(WithdrawRuleViolationException.class)
                .hasMessageContaining("이미 받은 뿌리기는 중복으로 다시 받을 수 없습니다.");
    }

    @Test
    @DisplayName("모두 소진된 뿌리기에 받기를 시도한다.")
    void withdrawError5() {
        LocalDateTime now = LocalDateTime.now();
        LuckyAccountCreateRequest luckyAccountCreateRequest = LuckyAccountCreateRequest.builder()
                .token("abc")
                .userId(1L)
                .roomId("room")
                .amount(100000)
                .withdrawLimit(2)
                .requestedAT(now)
                .build();
        luckyAccountService.create(luckyAccountCreateRequest);

        LuckyAccountWithdrawRequest luckyAccountWithdrawRequest1 = LuckyAccountWithdrawRequest.builder()
                .token("abc")
                .roomId("room")
                .userId(2L)
                .requestedAt(now.plusMinutes(1))
                .build();

        LuckyAccountWithdrawRequest luckyAccountWithdrawRequest2 = LuckyAccountWithdrawRequest.builder()
                .token("abc")
                .roomId("room")
                .userId(3L)
                .requestedAt(now.plusMinutes(1))
                .build();

        LuckyAccountWithdrawRequest luckyAccountWithdrawRequest3 = LuckyAccountWithdrawRequest.builder()
                .token("abc")
                .roomId("room")
                .userId(4L)
                .requestedAt(now.plusMinutes(1))
                .build();

        luckyAccountService.withdraw(luckyAccountWithdrawRequest1);
        luckyAccountService.withdraw(luckyAccountWithdrawRequest2);

        assertThatThrownBy(() -> luckyAccountService.withdraw(luckyAccountWithdrawRequest3))
                .isInstanceOf(WithdrawFailureException.class)
                .hasMessageContaining("더 이상 남은 뿌리기가 없습니다.");
    }
}