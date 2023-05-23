package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.Dtos.AccountDto;
import com.mindhub.Homebanking.Dtos.ClientDto;
import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.AccountType;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import com.mindhub.Homebanking.Services.AccountService;
import com.mindhub.Homebanking.Services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/api/accounts")
    public List<AccountDto> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/api/accounts/{id}")
    public AccountDto getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam String accountType) {
        Random random = new Random();
        int randomNumber = random.nextInt(99999999);
        String accountNumber = "VIN" + String.format("%08d", randomNumber);

        Client client = clientService.findByEmail(authentication.getName());
        List<Account> accountsActive = client.getAccounts().stream().filter(account -> account.isActive()).collect(Collectors.toList());
        Set<Account> accounts = client.getAccounts();

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Alcanzaste el límite de cuentas creadas", HttpStatus.FORBIDDEN);
        };
        if ( !accountType.equalsIgnoreCase("CORRIENTE") && !accountType.equalsIgnoreCase("AHORRO")){
            return new ResponseEntity<>("Selecciona un tipo de cuenta correcto", HttpStatus.FORBIDDEN);}

        Account accountGenerated = new Account(accountNumber, LocalDateTime.now(), 0.00, true, AccountType.valueOf(accountType.toUpperCase()));
        accountService.saveAccount(accountGenerated);
        client.addAccount(accountGenerated);
        clientService.saveClient(client);
        return new ResponseEntity<>( "Cuenta creada con éxito",HttpStatus.CREATED);

    }
    @PutMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount (
            Authentication authentication , @RequestParam long id){

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);

        if( account == null ){
            return new ResponseEntity<>("Esta cuenta no existe", HttpStatus.FORBIDDEN);
        } else if ( !account.isActive() ){
            return new ResponseEntity<>("Esta cuenta esta inactiva", HttpStatus.FORBIDDEN);
        } else if( account.getBalance() > 0 ){
            return new ResponseEntity<>("No puedes eliminar esta cuenta porque tiene dinero", HttpStatus.FORBIDDEN);
        }

        if (client == null) {
            return new ResponseEntity<>("No eres un cliente", HttpStatus.FORBIDDEN);
        }else if( client.getAccounts().stream().filter(account1 -> account1.getId() == id).collect(toList()).size() == 0 ){
            return new ResponseEntity<>("Esta cuenta no te pertenece", HttpStatus.FORBIDDEN);}

        account.setActive(false);
        account.getTransactions().stream().forEach( transaction -> transaction.setActive(false));
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

