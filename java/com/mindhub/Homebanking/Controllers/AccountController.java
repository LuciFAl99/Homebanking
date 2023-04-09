package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.Dtos.AccountDto;
import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController

public class AccountController {


        @Autowired

        private AccountRepository accountRepository;

        @RequestMapping("/api/accounts")
        public List<AccountDto> getAccounts() {
            return accountRepository.findAll().stream().map(account -> new AccountDto(account)).collect(toList());
           // return accountRepository.findAll().stream().map(account -> new AccountDto(account)).collect(toList());
        }
        @RequestMapping("/api/accounts/{id}")
        public AccountDto getAccount(@PathVariable Long id) {
            Optional<Account> optionalAccount = accountRepository.findById(id);
            return  optionalAccount.map(account -> new AccountDto(account)).orElse(null);

        }

}

