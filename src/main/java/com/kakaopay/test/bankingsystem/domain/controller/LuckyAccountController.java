package com.kakaopay.test.bankingsystem.domain.controller;

import com.kakaopay.test.bankingsystem.domain.dto.LuckyAccountCreateRequest;
import com.kakaopay.test.bankingsystem.domain.dto.LuckyAccountLookupRequest;
import com.kakaopay.test.bankingsystem.domain.dto.LuckyAccountWithdrawRequest;
import com.kakaopay.test.bankingsystem.domain.entity.Account;
import com.kakaopay.test.bankingsystem.domain.entity.Transaction;
import com.kakaopay.test.bankingsystem.domain.service.LuckyAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LuckyAccountController {

    private final LuckyAccountService luckyAccountService;

    public LuckyAccountController(LuckyAccountService luckyAccountService) {
        this.luckyAccountService = luckyAccountService;
    }

    @PostMapping("/lucky-accounts")
    public Account create(@RequestBody LuckyAccountCreateRequest request) {
        return luckyAccountService.create(request);
    }

    @GetMapping("/lucky-accounts/withdraw")
    public Transaction withdraw(@RequestBody LuckyAccountWithdrawRequest request) {
        return luckyAccountService.withdraw(request);
    }

    @GetMapping("/lucky-accounts/lookup")
    public List<Transaction> lookup(@RequestBody LuckyAccountLookupRequest request) {
        return luckyAccountService.lookup(request);
    }
}
