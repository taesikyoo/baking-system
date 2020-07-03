package com.kakaopay.test.bankingsystem.domain.controller;

import com.kakaopay.test.bankingsystem.domain.dto.*;
import com.kakaopay.test.bankingsystem.domain.service.LuckyAccountService;
import org.springframework.web.bind.annotation.*;

@RestController
public class LuckyAccountController {

    private final LuckyAccountService luckyAccountService;

    public LuckyAccountController(LuckyAccountService luckyAccountService) {
        this.luckyAccountService = luckyAccountService;
    }

    @PostMapping("/lucky-accounts")
    public LuckyAccountCreateResponse create(@RequestBody LuckyAccountCreateRequest request) {
        return luckyAccountService.create(request);
    }

    @GetMapping("/lucky-accounts/withdraw")
    public LuckyAccountWithdrawResponse withdraw(@RequestBody LuckyAccountWithdrawRequest request) {
        return luckyAccountService.withdraw(request);
    }

    @GetMapping("/lucky-accounts/lookup")
    public LuckyAccountLookupResponse lookup(@RequestBody LuckyAccountLookupRequest request) {
        return luckyAccountService.lookup(request);
    }
}
