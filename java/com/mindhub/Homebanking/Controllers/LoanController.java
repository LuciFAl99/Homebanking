package com.mindhub.Homebanking.Controllers;


import com.mindhub.Homebanking.Dtos.LoanApplicationDto;
import com.mindhub.Homebanking.Dtos.LoanDto;
import com.mindhub.Homebanking.Models.*;
import com.mindhub.Homebanking.Repositories.*;
import com.mindhub.Homebanking.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RestController
public class LoanController {
    @Autowired
    ClientLoanService clientLoanService;
    @Autowired
    AccountService accountService;
    @Autowired
    LoanService loanService;
    @Autowired
    ClientService clientService;
    @Autowired
    TransactionService transactionService;

    @GetMapping("/api/loans")
    public List<LoanDto> getLoans() {
        return loanService.getLoans();
    }
    @PostMapping("/api/loans")
    public ResponseEntity<Object> loans(
            Authentication authentication, @RequestBody LoanApplicationDto loanApplicationDto){

        Loan loan = this.loanService.findById(loanApplicationDto.getLoanId());
        Account account = this.accountService.findByNumber(loanApplicationDto.getDestinationAccountNumber());
        Client client = this.clientService.findByEmail(authentication.getName());


        //Verificar que los campos no esten vacíos o no sean inválidos
        StringBuilder errorMessage = new StringBuilder();
        if (loanApplicationDto.getDestinationAccountNumber().isBlank()) {
            errorMessage.append("El número de cuenta destino es requerido\n");
        }
        if (loanApplicationDto.getAmount() <= 0) {
            errorMessage.append("El monto es requerido y debe ser un número válido\n");
        }
        if (loanApplicationDto.getPayments() <= 0) {
            errorMessage.append("Cuotas es requerido y debe ser un número válido\n");
        }
        if (loanApplicationDto.getLoanId() == 0) {
            errorMessage.append("El tipo de préstamo es requerido\n");
        }

        if (errorMessage.length() > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage.toString());
        }

        //Verificar que el préstamo exista
        if (loan == null) {
            return new ResponseEntity<>("No existe el Préstamo", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta exista
        if (account == null) {
            return new ResponseEntity<>("La cuenta destino no existe", HttpStatus.FORBIDDEN);
        }

        //Verificar que el cliente exista
        if (client == null) {
            return new ResponseEntity<>("El cliente autenticado no existe", HttpStatus.FORBIDDEN);
        }
        //Verificar que el monto solicitado no exceda el monto máximo del préstamo
        if(loanApplicationDto.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("El monto solicitado supera el permitido", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        if (!loan.getPayments().contains(loanApplicationDto.getPayments())){
            return new ResponseEntity<>("Cantidad de cuotas incorrectas", HttpStatus.FORBIDDEN);
        }
        //Verificar que la cuenta de destino exista
        if(loanApplicationDto.getDestinationAccountNumber() == null){
            return new ResponseEntity<>("La cuenta destino no existe", HttpStatus.FORBIDDEN);
        }
        // Verificar que el cliente no tenga ya un préstamo del mismo tipo
        ClientLoan existingLoan = this.clientLoanService.findByLoanAndClient(loan, client);
        if (existingLoan != null) {
            return new ResponseEntity<>("El cliente ya tiene un préstamo del mismo tipo", HttpStatus.FORBIDDEN);
        }


        //Verificar que la cuenta de destino pertenezca al cliente autenticado
        if(!account.getClient().equals(client)){
            return new ResponseEntity<>("La cuenta destino no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
        }


        //Creo el prestamo del cliente le sumo el 20% y lo guardo
        ClientLoan clientLoan = new ClientLoan(loanApplicationDto.getAmount(),loanApplicationDto.getPayments());
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);
        clientLoan.setAmount(clientLoan.getAmount()*1.20);
        clientLoanService.saveClientLoan(clientLoan);

        //Creo la transaccion y la guardo
        Transaction creditTransaction = new Transaction(TransactionType.CREDITO, loanApplicationDto.getAmount(),loanApplicationDto.getLoanId()+"crédito aprobado", LocalDateTime.now(), true);
        transactionService.saveTransaction(creditTransaction);

        //Le asigno la transaccion a la cuenta de destino, le agrego el balance y la guardo
        account.addTransaction(creditTransaction);
        account.setBalance(account.getBalance()+creditTransaction.getAmount());
        accountService.saveAccount(account);

        return  new ResponseEntity<>("Create", HttpStatus.CREATED);
    }
    @PostMapping("/api/admin/loan")
    public ResponseEntity<Object> newLoanAdmin(@RequestBody Loan loan) {

        Loan newLoan = new Loan(loan.getName(), loan.getMaxAmount() , loan.getPayments());
        loanService.saveLoan(newLoan);

        return new ResponseEntity<>("Préstamo creado con éxito", HttpStatus.CREATED);
    }
}
