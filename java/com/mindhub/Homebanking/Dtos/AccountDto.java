package com.mindhub.Homebanking.Dtos;

import com.mindhub.Homebanking.Models.Account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDto {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    private List<TransactionDto> transactions;


    public AccountDto() {}

    public AccountDto(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransaction()
                .stream()
                .map(transaction -> new TransactionDto(transaction))
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<TransactionDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDto> transactions) {
        this.transactions = transactions;
    }
}