package th.ac.ku.bankaccount.controller;

import org.springframework.web.bind.annotation.*;
import th.ac.ku.bankaccount.data.BankAccountRepository;
import th.ac.ku.bankaccount.model.BankAccount;

import java.util.List;

@RestController
@RequestMapping("/api/bankaccount")
public class BankAccountRestController {

    private BankAccountRepository repository;

    public BankAccountRestController(BankAccountRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/customer/{customerId}")
    public List<BankAccount> getAllCustomerId(@PathVariable int customerId) {
        return repository.findByCustomerId(customerId);
    }

    @GetMapping
    public List<BankAccount> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public BankAccount getOne(@PathVariable int id) {
        return repository.findById(id).get();
    }

    @PostMapping
    public BankAccount create(@RequestBody BankAccount bankAccount){
        repository.save(bankAccount);
        return repository.findById(bankAccount.getId()).get();
    }

    @PostMapping("transaction/{id}")
    public BankAccount makeTransaction(@PathVariable int id,
                              @RequestBody th.ac.ku.atm.model.Transaction transaction) {
        BankAccount record = repository.findById(id).get();

        switch (transaction.getTransactiontype().toLowerCase()) {
            case "deposit": {
                record.deposit(transaction.getAmount());
                break;
            }
            case "withdraw": {
                record.withdraw(transaction.getAmount());
                break;
            }
            default: {
                throw new Error("cannot make transaction");
            }
        }

        repository.save(record);

        return record;
    }

    @DeleteMapping("/{id}")
    public BankAccount delete(@PathVariable int id) {
        BankAccount record = repository.findById(id).get();
        repository.deleteById(id);
        return record;
    }

}
