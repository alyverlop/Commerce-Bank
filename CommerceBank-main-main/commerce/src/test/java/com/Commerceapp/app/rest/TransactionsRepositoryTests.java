package com.Commerceapp.app.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TransactionsRepositoryTests {

    @Autowired
    private TransactionsRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateIncome() {
        Transactions transaction = new Transactions();
        transaction.setFromSource("3265716350");
        transaction.setToSource("Google");
        transaction.setAmount(5000);
        transaction.setTransaction_date();
        transaction.setRefNum();
        transaction.setTypeOf("Employer:");
        transaction.setCategory("Direct Deposit");
        transaction.setPlusorminus("+$");
        repo.save(transaction);
        Transactions savedTransaction = repo.save(transaction);

        Transactions existTransaction = entityManager.find(Transactions.class, savedTransaction.getId());

        assertThat(existTransaction.getFromSource()).isEqualTo(transaction.getFromSource());


    }

    @Test
    public void testCreateMisc() {
        Transactions transaction = new Transactions();
        transaction.setFromSource("3265716350");
        transaction.setToSource("H&R Block");
        transaction.setAmount(50);
        transaction.setTransaction_date();
        transaction.setRefNum();
        transaction.setTypeOf("Merchant:");
        transaction.setCategory("Misc");
        transaction.setPlusorminus("-$");
        repo.save(transaction);
        Transactions savedTransaction = repo.save(transaction);

        Transactions existTransaction = entityManager.find(Transactions.class, savedTransaction.getId());

        assertThat(existTransaction.getFromSource()).isEqualTo(transaction.getFromSource());


    }

    @Test
    public void testCreateRent() {
        Transactions transaction = new Transactions();
        transaction.setFromSource("3265716350");
        transaction.setToSource("Old Town Lofts");
        transaction.setAmount(2100);
        transaction.setTransaction_date();
        transaction.setRefNum();
        transaction.setTypeOf("Merchant:");
        transaction.setCategory("Rent");
        transaction.setPlusorminus("-$");
        repo.save(transaction);
        Transactions savedTransaction = repo.save(transaction);

        Transactions existTransaction = entityManager.find(Transactions.class, savedTransaction.getId());

        assertThat(existTransaction.getFromSource()).isEqualTo(transaction.getFromSource());


    }

    @Test
    public void testCreateFood() {
        Transactions transaction = new Transactions();
        transaction.setFromSource("3265716350");
        transaction.setToSource("Starbucks");
        transaction.setAmount(25);
        transaction.setTransaction_date();
        transaction.setRefNum();
        transaction.setTypeOf("Merchant:");
        transaction.setCategory("Foods");
        transaction.setPlusorminus("-$");
        repo.save(transaction);
        Transactions savedTransaction = repo.save(transaction);

        Transactions existTransaction = entityManager.find(Transactions.class, savedTransaction.getId());

        assertThat(existTransaction.getFromSource()).isEqualTo(transaction.getFromSource());


    }

    @Test
    public void testCreateTransport() {
        Transactions transaction = new Transactions();
        transaction.setFromSource("3265716350");
        transaction.setToSource("Jiffy Lube");
        transaction.setAmount(85);
        transaction.setTransaction_date();
        transaction.setRefNum();
        transaction.setTypeOf("Merchant:");
        transaction.setCategory("Transport");
        transaction.setPlusorminus("-$");
        repo.save(transaction);
        Transactions savedTransaction = repo.save(transaction);

        Transactions existTransaction = entityManager.find(Transactions.class, savedTransaction.getId());

        assertThat(existTransaction.getFromSource()).isEqualTo(transaction.getFromSource());


    }

    @Test
    public void testCreateBills() {
        Transactions transaction = new Transactions();
        transaction.setFromSource("3265716350");
        transaction.setToSource("Netflix");
        transaction.setAmount(30);
        transaction.setTransaction_date();
        transaction.setRefNum();
        transaction.setTypeOf("Merchant:");
        transaction.setCategory("Bills");
        transaction.setPlusorminus("-$");
        repo.save(transaction);
        Transactions savedTransaction = repo.save(transaction);

        Transactions existTransaction = entityManager.find(Transactions.class, savedTransaction.getId());

        assertThat(existTransaction.getFromSource()).isEqualTo(transaction.getFromSource());


    }



}
