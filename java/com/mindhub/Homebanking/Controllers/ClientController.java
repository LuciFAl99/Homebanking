
package com.mindhub.Homebanking.Controllers;


import com.mindhub.Homebanking.Dtos.ClientDto;
import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.AccountType;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Services.AccountService;
import com.mindhub.Homebanking.Services.ClientService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;



@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/api/clients")
    public List<ClientDto> getClients() {
        return clientService.getClients();
    }

    @GetMapping("/api/clients/{id}")
    public ClientDto getClient(@PathVariable Long id) {
        return clientService.getClient(id);
    }

    @GetMapping("api/clients/current")
    public ClientDto getCurrentClient(Authentication authentication) {
        return clientService.getCurrentClient(authentication);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping( "/api/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

            if (firstName.isBlank()){
                return new ResponseEntity<>("First name is required", HttpStatus.FORBIDDEN);
            } else if (lastName.isBlank()) {
                return new ResponseEntity<>("Last name is required", HttpStatus.FORBIDDEN);
            } else if (email.isBlank()) {
                return new ResponseEntity<>("email is required", HttpStatus.FORBIDDEN);
            } else if (password.isBlank()){
                return new ResponseEntity<>("password is required", HttpStatus.FORBIDDEN);
            } else if (password.length() < 8){
                return new ResponseEntity<>("La contraseña debe tener al menos 8 caracteres", HttpStatus.FORBIDDEN);
            }


        if (clientService.findByEmail(email) !=  null) {

            return new ResponseEntity<>("El email esta en uso", HttpStatus.FORBIDDEN);

        }
        Random random = new Random();
        int randomNumber = random.nextInt(99999999);
        String accountNumber = "VIN" + String.format("%08d", randomNumber);

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account (accountNumber, LocalDateTime.now(), 0.00, true, AccountType.AHORRO);
        clientService.saveClient(newClient);
        newClient.addAccount(newAccount);
        accountService.saveAccount(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}