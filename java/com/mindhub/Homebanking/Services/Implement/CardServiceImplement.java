package com.mindhub.Homebanking.Services.Implement;

import com.mindhub.Homebanking.Models.Card;
import com.mindhub.Homebanking.Repositories.CardRepository;
import com.mindhub.Homebanking.Services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;
    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
