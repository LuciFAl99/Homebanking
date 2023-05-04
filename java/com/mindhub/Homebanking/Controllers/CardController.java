package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.Models.Card;
import com.mindhub.Homebanking.Models.CardColor;
import com.mindhub.Homebanking.Models.CardType;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.CardRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color) {
        Random random = new Random();
        int cardCvv = random.nextInt(899) + 100;
        String cardNumber = "";
        for (int i = 0; i < 16; i++) {
            if (i > 0 && i % 4 == 0) {
                cardNumber += " ";
            }
            cardNumber += (int) (Math.random() * 10);
        }

        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getCards().stream().filter(e -> e.getType().toString().equals(type.toString())).count() >= 3) {
            return new ResponseEntity<>("403 Ya tiene 3 tarjetas de ese tipo", HttpStatus.FORBIDDEN);
        }
        Set<Card> cards = client.getCards().stream().filter(card -> card.getType()== type).collect(Collectors.toSet());
        if(cards.stream().anyMatch(card -> card.getColor()== color)){
            return new ResponseEntity<>("Ya tienes una tarjeta de ese tipo", HttpStatus.FORBIDDEN);
        }





        Card cardGenerated = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cardNumber, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
        cardRepository.save(cardGenerated);
        client.addCard(cardGenerated);
        clientRepository.save(client);
        return new ResponseEntity<>("Tarjeta creada con éxito", HttpStatus.CREATED);

    }
}
