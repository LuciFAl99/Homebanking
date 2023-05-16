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
    private boolean active;
    private List<TransactionDto> transactions;


    public AccountDto() {}

    public AccountDto(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransaction()
                .stream()
                .map(transaction -> new TransactionDto(transaction)).collect(Collectors.toList());
        this.active = account.isActive();
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public List<TransactionDto> getTransactions() {
        return transactions;
    }

    public boolean isActive() {
        return active;
    }
}