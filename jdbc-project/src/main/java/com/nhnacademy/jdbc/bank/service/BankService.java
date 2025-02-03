package com.nhnacademy.jdbc.bank.service;

import com.nhnacademy.jdbc.bank.domain.Account;
import java.sql.Connection;

public interface BankService {

    Account getAccount(Connection connection, long accountNumber);

    void createAccount(Connection connection, Account account);

    boolean depositAccount(Connection connection, long accountNumber, long amount);

    boolean withdrawAccount(Connection connection, long accountNumber, long amount);

    void transferAmount(Connection connection, long accountNumberFrom, long accountNumberTo, long amount);

    boolean isExistAccount(Connection connection, long accountNumber);

    void dropAccount(Connection connection, long accountNumber);
}
