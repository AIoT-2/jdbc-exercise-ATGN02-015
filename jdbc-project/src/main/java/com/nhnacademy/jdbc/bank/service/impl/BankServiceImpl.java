package com.nhnacademy.jdbc.bank.service.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.exception.AccountAlreadyExistException;
import com.nhnacademy.jdbc.bank.exception.AccountNotFoundException;
import com.nhnacademy.jdbc.bank.exception.BalanceNotEnoughException;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;
import com.nhnacademy.jdbc.bank.service.BankService;

import java.sql.Connection;
import java.util.Optional;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository;

    public BankServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccount(Connection connection, long accountNumber){
        //todo#11 계좌-조회
//        String sql = "select * from jdbc_account where account_number=?";
//        ResultSet rs = null;
//
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setLong(1, accountNumber);
//            rs = statement.executeQuery();
//
//            if (rs.next()) {
//                return new Account(rs.getLong("account_number"), rs.getString("name"), rs.getLong("balance"));
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                rs.close();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return null;

        Optional<Account> accountOptional = accountRepository.findByAccountNumber(connection, accountNumber);
        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundException(accountNumber);
        }
        // findByAccountNumber 메서드에서 Optional을 사용해 결과가 없으면 Optional.Empty()로 반환하게 했기 때문에 if로 확인 필요
        return accountOptional.get();
    }

    @Override
    public void createAccount(Connection connection, Account account){
        //todo#12 계좌-등록
        if (isExistAccount(connection, account.getAccountNumber())) {
            throw new AccountAlreadyExistException(account.getAccountNumber());
        }

        int result = accountRepository.save(connection, account);

        if (result != 1) {
            throw new RuntimeException("계좌 등록이 정상적으로 되지 않음");
        }
    }

    @Override
    public boolean depositAccount(Connection connection, long accountNumber, long amount){
        //todo#13 예금, 계좌가 존재하는지 체크 -> 예금실행 -> 성공 true, 실패 false;
        if (!isExistAccount(connection, accountNumber)) {
            throw new AccountNotFoundException(accountNumber);
        }

        int result = accountRepository.deposit(connection, accountNumber, amount);

        if (result != 1) {
            throw new RuntimeException("예금이 정상적으로 되지 않음");
        }

        return true;
    }

    @Override
    public boolean withdrawAccount(Connection connection, long accountNumber, long amount){
        //todo#14 출금, 계좌가 존재하는지 체크 ->  출금가능여부 체크 -> 출금실행, 성공 true, 실폐 false 반환
        if (!isExistAccount(connection, accountNumber)) {
            throw new AccountNotFoundException(accountNumber);
        }

        Account account = getAccount(connection, accountNumber);

//        if (account.getBalance() - amount < 0) {
//            return false;
//        }

        if (!account.isWithdraw(amount)) {
            throw new BalanceNotEnoughException(accountNumber);
        }

        int result = accountRepository.withdraw(connection, accountNumber, amount);

        if (result != 1) {
            throw new RuntimeException("출금이 정상적으로 되지 않음");
        }

        return true;
    }

    @Override
    public void transferAmount(Connection connection, long accountNumberFrom, long accountNumberTo, long amount){
        //todo#15 계좌 이체 accountNumberFrom -> accountNumberTo 으로 amount만큼 이체
        if (!isExistAccount(connection, accountNumberTo) || !isExistAccount(connection, accountNumberFrom)) {
            throw new AccountNotFoundException(accountNumberTo, accountNumberFrom);
        }

        Account accountFrom = getAccount(connection, accountNumberFrom);
        Account accountTo = getAccount(connection, accountNumberTo);

//        if (!withdrawAccount(connection, accountNumberFrom, amount)) {
//            throw new BalanceNotEnoughException(accountNumberFrom);
//        }

        if (!accountFrom.isWithdraw(amount)) {
            throw new BalanceNotEnoughException(accountNumberFrom);
        }

        int withdraw_result = accountRepository.withdraw(connection, accountNumberFrom, amount);
        int deposit_result = accountRepository.deposit(connection, accountNumberTo, amount);

        if (withdraw_result != 1 || deposit_result != 1) {
            throw new RuntimeException("이체가 정상적으로 되지 않음");
        }

    }

    @Override
    public boolean isExistAccount(Connection connection, long accountNumber){
        //todo#16 Account가 존재하면 true , 존재하지 않다면 false
//        if (accountRepository.findByAccountNumber(connection, accountNumber).isEmpty()) {
//            return false;
//        }
//        return true;

//        return accountRepository.findByAccountNumber(connection, accountNumber).isPresent();

        int count =  accountRepository.countByAccountNumber(connection, accountNumber);

        if (count != 1) {
            return false;
        }

        return true;
    }

    @Override
    public void dropAccount(Connection connection, long accountNumber) {
        //todo#17 account 삭제
        if (!isExistAccount(connection, accountNumber)) {
            throw new AccountNotFoundException(accountNumber);
        }

        int result = accountRepository.deleteByAccountNumber(connection, accountNumber);

        if (result != 1) {
            throw new RuntimeException("삭제가 정상적으로 되지 않음");
        }
    }

}