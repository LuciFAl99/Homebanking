package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Transaction;
import com.mindhub.Homebanking.Models.TransactionType;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import com.mindhub.Homebanking.Repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Transactional
@RestController
public class TransactionController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @PostMapping("/api/clients/current/transactions")
    public ResponseEntity<Object> transaction(
            Authentication authentication, @RequestParam double amount, @RequestParam String description,
            @RequestParam String accountOriginNumber, @RequestParam String destinationAccountNumber) {

        Account originAccount = accountRepository.findByNumber(accountOriginNumber.toUpperCase());
        Account destinationAccount = accountRepository.findByNumber(destinationAccountNumber.toUpperCase());

        //Verificar que los parámetros no estén en blanco
        StringBuilder errorMessage = new StringBuilder();
        if (description.isBlank()) {
            errorMessage.append("Description es requerido\n");
        }
        if (accountOriginNumber.isBlank()) {
            errorMessage.append("El número de cuenta es requerido\n");
        }
        if (destinationAccountNumber.isBlank()) {
            errorMessage.append("La cuenta de destino es requerida\n");
        }
        if (amount == 0.0 || Double.isNaN(amount)) {
            errorMessage.append("Amount es requerido y debe ser un número válido\n");
        }

        if (errorMessage.length() > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage.toString());
        }

        //Verifica que el monto no sea inválido
        if (amount < 1) {
            return new ResponseEntity<>("Monto inválido", HttpStatus.FORBIDDEN);
        }

        //Verificar que los números de cuenta no sean iguales
        if (accountOriginNumber.equals(destinationAccountNumber)) {
            return new ResponseEntity<>("Los números de cuenta son iguales", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de origen exista
        if (originAccount == null) {
            return new ResponseEntity<>("La cuenta de origen no existe", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de origen pertenece al cliente autenticado
        if (authentication == null || !originAccount.getClient().getEmail().equals(authentication.getName())) {
            return new ResponseEntity<>("La cuenta de origen no te pertenece", HttpStatus.FORBIDDEN);
        }

        //Verificar que exista la cuenta destino
        if (destinationAccount == null) {
            return new ResponseEntity<>("La cuenta destino no existe", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de origen tenga el monto disponible
        if (originAccount.getBalance() < amount) {
            return new ResponseEntity<>("La cuenta de origen no tiene fondos suficientes", HttpStatus.FORBIDDEN);
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBITO, -amount, accountOriginNumber + " " + description, LocalDateTime.now());
        Transaction creditTransaction = new Transaction(TransactionType.CREDITO, amount, destinationAccountNumber + " " + description, LocalDateTime.now());

        originAccount.addTransaction(debitTransaction);
        originAccount.addTransaction(creditTransaction);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        //Actualizar cuentas con los montos correspondientes
        originAccount.setBalance(originAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        //Guardar cuentas actualizadas a través del repositorio de cuentas
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        return new ResponseEntity<>("Transacción exitosa", HttpStatus.CREATED);
    }
}
