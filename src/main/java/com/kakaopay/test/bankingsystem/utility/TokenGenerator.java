package com.kakaopay.test.bankingsystem.utility;

import java.util.UUID;

public class TokenGenerator {

    public static String generateToken() {
        return UUID.randomUUID().toString().substring(0, 3);
    }
}
