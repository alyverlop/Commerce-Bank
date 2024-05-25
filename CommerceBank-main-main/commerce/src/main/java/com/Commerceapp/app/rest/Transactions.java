package com.Commerceapp.app.rest;

import jakarta.persistence.*;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false, length = 45)
    private String fromSource;

    @Column(nullable = false, unique = false, length = 45)
    private String toSource;

    @Column(nullable = false, unique = false, length = 45)
    private double amount;

    @Column(nullable = false, unique = false, length = 10)
    private String transaction_date;

    @Column(nullable = false, unique = true, length = 20)
    private String RefNum;


    @Column(nullable = false, unique = false, length = 20)
    private String typeOf;


    @Column(nullable = false, unique = false, length = 20)
    private String category;


    @Column(nullable = false, unique = false, length = 2)
    private String plusorminus;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromSource() {
        return fromSource;
    }

    public void setFromSource(String fromSource) {
        this.fromSource = fromSource;
    }

    public String getToSource() {
        return toSource;
    }

    public void setToSource(String toSource) {
        this.toSource = toSource;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        this.transaction_date = now.format(formatter);
    }

//    public void setTransaction_date(String transaction_date) {
//        this.transaction_date = transaction_date;
//    }

    public String getRefNum() {
        return RefNum;
    }

    public void setRefNum() {

        int length = 15;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Append a random digit (0-9)
        }

        this.RefNum = sb.toString();
    }

    public String getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(String typeOf) {
        this.typeOf = typeOf;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlusorminus() {
        return plusorminus;
    }

    public void setPlusorminus(String plusorminus) {
        this.plusorminus = plusorminus;
    }


}
