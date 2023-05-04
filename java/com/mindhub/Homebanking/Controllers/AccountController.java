package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.Dtos.AccountDto;
import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/api/accounts")
    public List<AccountDto> getAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDto(account)).collect(toList());
    }

    @RequestMapping("/api/accounts/{id}")
    public AccountDto getAccount(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(account -> new AccountDto(account))
                .orElse(null);
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<?> createAccount(Authentication authentication) {
        Random random = new Random();
        int randomNumber = random.nextInt(99999999);
        String accountNumber = "VIN" + String.format("%08d", randomNumber);

        Client client = clientRepository.findByEmail(authentication.getName());
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Already Have 3 accounts", HttpStatus.FORBIDDEN);
        };
        Account accountGenerated = new Account(accountNumber, LocalDateTime.now(), 0.00);
        accountRepository.save(accountGenerated);
        client.addAccount(accountGenerated);
        clientRepository.save(client);
        return new ResponseEntity<>( HttpStatus.CREATED);
    }
}

