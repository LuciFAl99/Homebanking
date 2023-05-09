package com.mindhub.Homebanking.Services;

import com.mindhub.Homebanking.Models.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
}
