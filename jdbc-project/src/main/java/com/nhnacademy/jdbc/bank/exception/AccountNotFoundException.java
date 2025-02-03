package com.nhnacademy.jdbc.bank.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(long accountNumber) {
        super("Account Not Found accountNumber : " + accountNumber );
    }

    public AccountNotFoundException(long accountNumberTo, long accountNumberFrom) {
        super("계좌가 올바르지 않음");
    }
}
