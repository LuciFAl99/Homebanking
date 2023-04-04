package com.mindhub.Homebanking.Controllers;


import com.mindhub.Homebanking.Dtos.ClientDto;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
        Optional<Client> optionalClient = clientRepository.findById(id);
        return  optionalClient.map(client -> new ClientDto(client)).orElse(null);

    }



}

