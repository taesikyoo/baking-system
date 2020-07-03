package com.kakaopay.test.bankingsystem.domain.controller;

import com.kakaopay.test.bankingsystem.domain.dto.LuckyAccountCreateRequest;
import com.kakaopay.test.bankingsystem.domain.entity.Account;
import com.kakaopay.test.bankingsystem.domain.service.LuckyAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
