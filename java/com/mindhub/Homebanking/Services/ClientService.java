package com.mindhub.Homebanking.Services;

import com.mindhub.Homebanking.Dtos.ClientDto;
import com.mindhub.Homebanking.Models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    List<ClientDto> getClients();
    ClientDto getClient(Long id);
    void saveClient(Client client);

    ClientDto getCurrentClient(Authentication authentication);

    Client findByEmail(String email);
}
