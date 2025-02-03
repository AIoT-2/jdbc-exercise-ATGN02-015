package com.nhnacademy.jdbc.bank.domain;

import java.util.Objects;

public class Account {

    private long accountNumber;
    private String name;
    private long balance;

    public Account(long accountNumber, String name, long balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public long getBalance() {
        return balance;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isWithdraw(long amount){
        return balance-amount >= 0;
    }
    // 출금이 가능한 금액인지 확인

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // 같은 객체이면 바로 true 반환
        if (o == null || getClass() != o.getClass()) return false;
        // 파라미터인 o가 null 이거나, 비교하기 위한 두 객체의 클래스가 다르다면 바로 false 반환
        Account account = (Account) o;
        return accountNumber == account.accountNumber && balance == account.balance && Objects.equals(name, account.name);
        // o를 Account 객체로 변환해서 각각의 속성을 비교해서 다 같으면 true 반환
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, name, balance);
    }
    // equals가 true 반환하는 두 객체는 반드시 hasCode 값도 같아야 함
    // 그러나, hasCode가 동일하다고 equals가 true를 반환하는 것은 아님
    // -> equals와 hasCode 일관되게 오버라이드 해야 함

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
