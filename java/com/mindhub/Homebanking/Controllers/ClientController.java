
package com.mindhub.Homebanking.Controllers;


import com.mindhub.Homebanking.Dtos.ClientDto;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static java.util.stream.Collectors.toList;


@RestController
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/api/clients")
    public List<ClientDto> getClients() {
        return clientRepository.findAll().stream().map(client -> new ClientDto(client)).collect(toList());
    }

    @RequestMapping("/api/clients/{id}")
    public ClientDto getClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(client -> new ClientDto(client))
                .orElse(null);
    }

    @GetMapping("api/clients/current")
    public ClientDto getAll(Authentication authentication) {
        return new ClientDto(clientRepository.findByEmail(authentication.getName()));
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping( "/api/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }



        if (clientRepository.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }



        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

        return new ResponseEntity<>(HttpStatus.CREATED);

    }


}