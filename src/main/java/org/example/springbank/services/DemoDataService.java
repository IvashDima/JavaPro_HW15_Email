package org.example.springbank.services;

import org.example.springbank.enums.CurrencyType;
import org.example.springbank.enums.TransactionType;
import org.example.springbank.enums.UserRole;
import org.example.springbank.models.Account;
import org.example.springbank.models.Client;
import org.example.springbank.models.Transaction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoDataService {
    public static final String ADMIN_LOGIN = "admin";
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final ClientService clientService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public DemoDataService(UserService userService, PasswordEncoder encoder, ClientService clientService, AccountService accountService, TransactionService transactionService) {
        this.userService = userService;
        this.encoder = encoder;
        this.clientService = clientService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Transactional
    public void generateDemoData() {
        clientService.deleteAllClients();
        accountService.deleteAllAccounts();
        transactionService.deleteAllTransactions();

        Client clientadmin = new Client(ADMIN_LOGIN, ADMIN_LOGIN, "1234567", ADMIN_LOGIN + "@test.com", "address");
        System.out.println("CLIENT IN DEMO!!!"+clientadmin);
        clientService.addClient(clientadmin);
        userService.addUser(ADMIN_LOGIN + "@test.com",
                encoder.encode("password"),
                UserRole.ADMIN, clientadmin, ADMIN_LOGIN);

        Client client;
        Account account;
        Transaction transaction;

        for (int i = 1; i < 3; i++) {
            client = new Client("Name" + i, "Surname" + i, "1234567" + i, "user" + i + "@test.com", "address"+i);
            clientService.addClient(client);

            userService.addUser("user" + i + "@test.com",
                    encoder.encode("password"),
                    UserRole.USER, client,"Name" + i);

            for (CurrencyType currencyType : CurrencyType.values()){
                account = new Account(client, 0, currencyType);
                accountService.addAccount(account);
                transaction = new Transaction(account, account, 1000, TransactionType.DEPOSIT);
                transactionService.deposit(transaction);
            }
        }
    }
}
