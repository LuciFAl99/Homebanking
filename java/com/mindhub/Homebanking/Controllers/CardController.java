package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.Models.Card;
import com.mindhub.Homebanking.Models.CardColor;
import com.mindhub.Homebanking.Models.CardType;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.CardRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import com.mindhub.Homebanking.Services.CardService;
import com.mindhub.Homebanking.Services.ClientService;
import com.mindhub.Homebanking.Utils.CardUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
public class CardController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color) {


        Client client = clientService.findByEmail(authentication.getName());

        if (client.getCards().stream().filter(e -> e.getType().toString().equals(type.toString())).count() >= 3) {
            return new ResponseEntity<>("403 Ya tiene 3 tarjetas de ese tipo", HttpStatus.FORBIDDEN);
        }
        Set<Card> cards = client.getCards().stream().filter(card -> card.getType() == type).collect(Collectors.toSet());
        if (cards.stream().anyMatch(card -> card.getColor() == color)) {
            return new ResponseEntity<>("Ya tienes una tarjeta de ese tipo", HttpStatus.FORBIDDEN);
        }

        String cardNumber = CardUtils.getCardNumber();
        int cvv = CardUtils.getCVV();

        Card cardGenerated = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cardNumber, cvv, LocalDate.now(), LocalDate.now().plusYears(5));
        cardService.saveCard(cardGenerated);
        client.addCard(cardGenerated);
        clientService.saveClient(client);
        return new ResponseEntity<>("Tarjeta creada con éxito", HttpStatus.CREATED);

    }

    @PatchMapping("/api/clients/current/cards/delete")
    public ResponseEntity<Object> deleteAccount(@RequestParam String number) {
        Card deletedCard = cardService.findByNumber(number);
        cardService.saveCard(deletedCard);
        return new ResponseEntity<>("Card deleted", HttpStatus.OK);
    }
}