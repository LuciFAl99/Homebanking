package com.mindhub.Homebanking;

import com.mindhub.Homebanking.Models.*;
import com.mindhub.Homebanking.Repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static com.mindhub.Homebanking.Models.TransactionType.CREDITO;
import static com.mindhub.Homebanking.Models.TransactionType.DEBITO;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository){
		return (args) -> {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime tomorrow = now.plusDays(1);
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Gonzalo", "Martinez", "gonzalo@mindhub.com");
			Client client3 = new Client("Maria", "Gonzalez", "maria@mindhub.com");

			Account account1 = new Account("VIN001", now, 5000);
			Account account2 = new Account("VIN002", tomorrow, 7500);
			Account account3 = new Account("VIN003", now, 7700);
			Account account4 = new Account("VIN004", tomorrow, 87700);
			Account account5 = new Account("VIN005", now, 9876);

			Transaction transaction1 = new Transaction(DEBITO, -2223, "TransacciónPrimera", tomorrow);
			Transaction transaction2 = new Transaction(CREDITO, 4576, "Transacción", tomorrow);
			Transaction transaction3 = new Transaction(DEBITO, -690576, "TransacciónUltima", now);
			Transaction transaction4 = new Transaction(CREDITO, 7876, "Transacción4", tomorrow);
			Transaction transaction5 = new Transaction(DEBITO, -3476, "Transacción5", tomorrow);
			Transaction transaction6 = new Transaction(DEBITO, -6576, "Transacción6", now);

			repository.save(client1);
			repository.save(client2);
			repository.save(client3);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);
			client3.addAccount(account5);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);

			repository.save(client1);
			repository.save(client2);
			repository.save(client3);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account3.addTransaction(transaction4);
			account4.addTransaction(transaction5);
			account5.addTransaction(transaction6);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);



			Loan loan1 = new Loan("Hipotecario", 500000, new ArrayList<>(Arrays.asList(12, 24, 36, 48, 60)));
			Loan loan2 = new Loan("Personal", 100000, new ArrayList<>(Arrays.asList(6, 12, 24)));
			Loan loan3 = new Loan("Automotriz", 300000, new ArrayList<>(Arrays.asList(6, 12, 24, 36)));

			ClientLoan clientLoan1 = new ClientLoan(400000, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan3);
			loan3.addClientLoan(clientLoan4);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);



		};
	}
}


