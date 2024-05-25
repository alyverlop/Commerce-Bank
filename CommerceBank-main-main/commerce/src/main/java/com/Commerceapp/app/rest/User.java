package com.Commerceapp.app.rest;

import jakarta.persistence.*;
import java.security.SecureRandom;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true, unique = true, length = 45)
    private String email;

    @Column(nullable = true, unique = true, length = 12)
    private String accountNum;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false, length = 70)
    private String address;

    @Column(nullable = false, length = 10)
    private String phoneNum;

    @Column(nullable = true, length = 64)
    private double balance;

    @Column(nullable = false, length = 20)
    private String firstName;
    @Column(nullable = false, length = 20)
    private String lastName;
    
    @Column(nullable = true, length = 20)
    private String dob;
    
    @Column(nullable = true, length = 20)
    private String transferBank;


    @Column(nullable = true, length = 6)
    private String otp;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum() {
    	
    	int length = 10;
   
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Append a random digit (0-9)
        }

        this.accountNum = sb.toString();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getTransferBank() {
		return transferBank;
	}

	public void setTransferBank(String transferBank) {
		this.transferBank = transferBank;
	}

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }





}
