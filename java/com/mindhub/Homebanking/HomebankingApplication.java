package com.mindhub.Homebanking;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Models.Transaction;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import com.mindhub.Homebanking.Repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

import static com.mindhub.Homebanking.Models.TransactionType.CREDITO;
import static com.mindhub.Homebanking.Models.TransactionType.DEBITO;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return (args) -> {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime tomorrow = now.plusDays(1);
			Client cliente1 = new Client ("Melba", "Morel", "melba@mindhub.com");
			Client cliente2 = new Client ("Gonzalo", "Martinez", "gonzalo@mindhub.com");
			Client cliente3 = new Client ("Maria", "Gonzalez", "maria@mindhub.com");
			repository.save(cliente1);
			repository.save(cliente2);
			repository.save(cliente3);

			Account account1 = new Account("VIN001", now, 5000, cliente1);
			Account account2 = new Account("VIN002", tomorrow, 7500, cliente1);
			Account account3 = new Account("VIN003", now, 7700, cliente2);
			Account account4 = new Account("VIN004", tomorrow, 87700, cliente3);
			Account account5 = new Account("VIN005", now, 9876, cliente2);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);

			Transaction transaction1 = new Transaction(DEBITO, -2223, "TransacciónPrimera", tomorrow, account1);
			Transaction transaction2 = new Transaction(CREDITO, 4576, "Transacción", tomorrow, account2);
			Transaction transaction3 = new Transaction(DEBITO, -690576, "TransacciónUltima", now, account2);
			Transaction transaction4 = new Transaction(CREDITO, 7876, "Transacción4", tomorrow, account3);
			Transaction transaction5 = new Transaction(DEBITO, -3476, "Transacción5",tomorrow, account4);
			Transaction transaction6 = new Transaction(DEBITO, -6576, "Transacción6", now, account5);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);




		};
	}
}


