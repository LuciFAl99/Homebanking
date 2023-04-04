package com.mindhub.Homebanking;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
		System.out.println("Hola");

	}
	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository accountRepository){
		return (args) -> {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime tomorrow = now.plusDays(1);
			Client cliente1 = new Client ("Melba", "Morel", "melba@mindhub.com");
			Client cliente2 = new Client ("Gonzalo", "Martinez", "gonzalo@mindhub.com");
			Client cliente3 = new Client ( "Maria", "Gonzalez", "maria@mindhub.com");
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



		};
	}
}


