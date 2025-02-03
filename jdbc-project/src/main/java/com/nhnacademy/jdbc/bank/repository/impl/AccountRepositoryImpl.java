package com.nhnacademy.jdbc.bank.repository.impl;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.exception.AccountNotFoundException;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;

import javax.swing.text.html.Option;
import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    public Optional<Account> findByAccountNumber(Connection connection, long accountNumber){
        //todo#1 계좌-조회
        String sql = "select * from jdbc_account where account_number=?";
        ResultSet rs = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountNumber);
            rs = statement.executeQuery();

            if (rs.next()) {
                int index = 0;
                Account account = new Account(rs.getLong("account_number"), rs.getString("name"), rs.getLong("balance"));

                return Optional.of(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public int save(Connection connection, Account account) {
        //todo#2 계좌-등록, executeUpdate() 결과를 반환 합니다.
        String sql = "insert into jdbc_account(account_number, name, balance) values (?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, account.getAccountNumber());
            statement.setString(2, account.getName());
            statement.setLong(3, account.getBalance());

//            int result = statement.executeUpdate();
//            return result;

            return statement.executeUpdate();
            // executeUpdate() 메서드는 int 형으로 반환되는 메서드
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int countByAccountNumber(Connection connection, long accountNumber){
        int count=0;
        //todo#3 select count(*)를 이용해서 계좌의 개수를 count해서 반환
        String sql = "select count(*) as count from jdbc_account where account_number=?";
        ResultSet rs = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountNumber);
            rs = statement.executeQuery();

            if (rs.next()) {
                count = rs.getInt("count");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return count;
    }

    @Override
    public int deposit(Connection connection, long accountNumber, long amount){
        //todo#4 입금, executeUpdate() 결과를 반환 합니다.
//        String sql = "update jdbc_account set balance=? where account_number=?";
//        String select_sql = "select balance from jdbc_account where account_number=?";
//        ResultSet rs = null;
//
//        try (PreparedStatement statement = connection.prepareStatement(sql);
//            PreparedStatement select_statement = connection.prepareStatement(select_sql)
//        ) {
//            select_statement.setLong(1, accountNumber);
//            rs = statement.executeQuery();
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

//        Account account = findByAccountNumber(connection, accountNumber)
//                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
//        // account가 null이면 예외 처리까지 하도록
//
////        if (Objects.isNull(account)) {
////            throw new AccountNotFoundException(accountNumber);
////        }
//
//        long balance = account.getBalance();
//
//        String sql = "update jdbc_account set balance=? where account_number=?";
//
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setLong(1, balance + amount);
//            statement.setLong(2, accountNumber);
//
//            return statement.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        String sql = "update jdbc_account set balance = balance + ? where account_number=? ";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,amount);
            statement.setLong(2,accountNumber);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int withdraw(Connection connection, long accountNumber, long amount){
        //todo#5 출금, executeUpdate() 결과를 반환 합니다.
        String sql = "update jdbc_account set balance = balance - ? where account_number=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, amount);
            statement.setLong(2, accountNumber);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int deleteByAccountNumber(Connection connection, long accountNumber) {
        //todo#6 계좌 삭제, executeUpdate() 결과를 반환 합니다.
        String sql = "delete from jdbc_account where account_number=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountNumber);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
