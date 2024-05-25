package com.Commerceapp.app.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TransactionsRepository repo2;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }

    @Transactional
    public boolean transferAmount(String fromAccountNum, String toAccountNum, double amount) {
        User fromUser = repo.findByAccountNum(fromAccountNum).orElse(null);
        User toUser = repo.findByAccountNum(toAccountNum).orElse(null);

        if (fromUser == null || toUser == null || fromUser.getBalance() < amount) {
            return false;
        }

        fromUser.setBalance(fromUser.getBalance() - amount);
        toUser.setBalance(toUser.getBalance() + amount);

        repo.save(fromUser);
        repo.save(toUser);

        return true;
    }

    @Transactional
    public boolean transferAmounts(String fromEmail, String toAccountNum, double amount) {
        User fromUser = repo.findByEmail(fromEmail);
        User toUser = repo.findByAccountNum(toAccountNum).orElse(null);

        if (fromUser == null || toUser == null || fromUser.getBalance() < amount) {
            return false;
        }

        fromUser.setBalance(fromUser.getBalance() - amount);
        toUser.setBalance(toUser.getBalance() + amount);

        repo.save(fromUser);
        repo.save(toUser);

        return true;
    }

    public boolean quickCheck(String ogEmail, String checkEmail, String checkPassword) {
        if (Objects.equals(ogEmail, checkEmail)) {
            User user = repo.findByEmail(checkEmail);
            if (user == null) {
                return false; // User not found
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(checkPassword, user.getPassword());
        } else {
            return false;
        }
    }
    public boolean quickCheck2(String accountNum, String checkPassword) {
        User user = repo.findByAccountNum2(accountNum);
        if (user == null) {
            return false; // User not found
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(checkPassword, user.getPassword());
        }
    }


    public void newTransaction(String fromSource, String toSource, double amount) {
        User user = repo.findByEmail(fromSource);
        User toUser = repo.findByAccountNum(toSource).orElse(null);
        Transactions sender = new Transactions();
        sender.setFromSource(user.getAccountNum());
        sender.setToSource("******" + toSource.substring(6));
        sender.setAmount(amount);
        sender.setTransaction_date();
        sender.setRefNum();
        sender.setTypeOf("Recipient:");
        sender.setCategory("Transfer");
        sender.setPlusorminus("-$");
        repo2.save(sender);
        Transactions receiver = new Transactions();
        receiver.setFromSource(toSource);
        receiver.setToSource("******" + user.getAccountNum().substring(6));
        receiver.setAmount(amount);
        receiver.setTransaction_date();
        receiver.setRefNum();
        receiver.setTypeOf("Sender:");
        receiver.setCategory("Transfer");
        receiver.setPlusorminus("+$");
        repo2.save(receiver);
    }

    /*public void defaultPieChart(String accountNum) {
        List<Transactions> transactions = repo2.findBySource(accountNum);
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        getMatchingAmounts(transactions, now.format(formatter));
    }*/

    public double getMatchingAmounts(String accountNum) {
        List<Transactions> transactions = repo2.findByDeposit(accountNum);
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String mydate = now.format(formatter);
        double total = 0;

        // Extract month and year from mydate (assuming mydate is in mm/dd/yyyy format)
        String myMonth = mydate.substring(0, 2);
        String myYear = mydate.substring(mydate.length() - 4);

        for (Transactions transaction : transactions) {
            String transactionDate = transaction.getTransaction_date();
            // Ensure the transactionDate is in the expected format to avoid StringIndexOutOfBoundsException
            if (transactionDate != null && transactionDate.length() == 10) {
                String transactionMonth = transactionDate.substring(0, 2);
                String transactionYear = transactionDate.substring(transactionDate.length() - 4);

                // Check if the month and year match
                if (myMonth.equals(transactionMonth) && myYear.equals(transactionYear)) {
                    // Add the amount to the list
                    total += transaction.getAmount();
                }
            }
        }

        return total;
    }

    public static double[] pie(double value) {
        double[] pie = new double[6];
        pie[0] = value * 0.4;  // 40%
        pie[1] = value * 0.1;  // 10%
        pie[2] = value * 0.05; // 5%
        pie[3] = value * 0.15; // 15%
        pie[4] = value * 0.1;  // 10% again
        pie[5] = value * 0.2;  // 20%
        return pie;
    }

    public static double[] pie2(double rent , double rent1, double bills , double bills1, double transport , double transport1, double food , double food1, double misc , double misc1, double savings , double savings1) {
        double[] pie = new double[6];
        pie[0] = rent - rent1;  // 40%
        pie[1] = bills - bills1;  // 10%
        pie[2] = transport - transport1; // 5%
        pie[3] = food - food1;
        pie[4] = misc - misc1; // 15%// 10% again
        pie[5] = savings - savings1;  // 20%
        return pie;
    }

    public static double decimal(double num) {
        double number = 854449.455456567890876543; // Example double variable

        // Create a DecimalFormat object with pattern "#.##" to format the number
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        // Format the number using DecimalFormat
        String formattedNumberStr = decimalFormat.format(num);

        // Parse the formatted string back to a double
        double formattedNumber = Double.parseDouble(formattedNumberStr);

        return formattedNumber;

    }

    public static String[] pie3(double[] pie){
    String[] pie3 = new String[6];
    for (int i = 0; i < 6; i++) {
        if (pie[i] > 0) {
            pie3[i] = "under";
        } else {
            pie3[i] = "over";
        }
    }
    return pie3;
    }

    public double last30days(List<Transactions>transactions){
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String mydate = now.format(formatter);
        double total = 0;

        // Extract month and year from mydate (assuming mydate is in mm/dd/yyyy format)
        String myMonth = mydate.substring(0, 2);
        String myYear = mydate.substring(mydate.length() - 4);

        for (Transactions transaction : transactions) {
            String transactionDate = transaction.getTransaction_date();
            // Ensure the transactionDate is in the expected format to avoid StringIndexOutOfBoundsException
            if (transactionDate != null && transactionDate.length() == 10) {
                String transactionMonth = transactionDate.substring(0, 2);
                String transactionYear = transactionDate.substring(transactionDate.length() - 4);

                // Check if the month and year match
                if (myMonth.equals(transactionMonth) && myYear.equals(transactionYear)) {
                    // Add the amount to the list
                    total += transaction.getAmount();
                }
            }
        }

        return total;
    }

    public double getRent(String accountNum) {
        List<Transactions> transactions = repo2.findByRent(accountNum);
        return last30days(transactions);
    }

    public double getBills(String accountNum) {
        List<Transactions> transactions = repo2.findByFromSource(accountNum);
        List<Transactions> transact = new ArrayList<>();

        for (Transactions transaction : transactions) {
            if (Objects.equals(transaction.getCategory(), "Bills")) {
                transact.add(transaction);
            }
        }
        return last30days(transact);
    }

    public double getTransport(String accountNum) {
        List<Transactions> transactions = repo2.findByFromSource(accountNum);
        List<Transactions> transact = new ArrayList<>();

        for (Transactions transaction : transactions) {
            if (Objects.equals(transaction.getCategory(), "Transport")) {
                transact.add(transaction);
            }
        }
        return last30days(transact);
    }

    public double getFood(String accountNum) {
        List<Transactions> transactions = repo2.findByFood(accountNum);
        return last30days(transactions);
    }

    public double getMisc(String accountNum) {
        List<Transactions> transactions = repo2.findByFromSource(accountNum);
        List<Transactions> transact = new ArrayList<>();

        for (Transactions transaction : transactions) {
            if (Objects.equals(transaction.getCategory(), "Misc")) {
                transact.add(transaction);
            }
        }
        return last30days(transact);
    }



/*    public double getBudget(String accountNum, String budget) {
        if (budget.equals("Rent")) {
            List<Transactions>transactions = repo2.findByRent(accountNum);
            return last30days(transactions);
        }
        else if (budget.equals("Bills")) {
            List<Transactions>transactions = repo2.findByBills(accountNum);
            return last30days(transactions);
        }
        else if (budget.equals("Transportation")) {
            List<Transactions>transactions = repo2.findByTrans(accountNum);
            return last30days(transactions);
        }
        else if (budget.equals("Food")) {
            List<Transactions>transactions = repo2.findByFood(accountNum);
            return last30days(transactions);
        }
        else if (budget.equals("Misc")) {
            List<Transactions>transactions = repo2.findByMisc(accountNum);
            return last30days(transactions);
        }
        else {
            return 0;
        }
    }*/

       /* for (Transactions transaction : transactions) {
            String transactionDate = transaction.getTransaction_date();
            // Ensure the transactionDate is in the expected format to avoid StringIndexOutOfBoundsException
            if (transactionDate != null && transactionDate.length() == 10) {
                String transactionMonth = transactionDate.substring(0, 2);
                String transactionYear = transactionDate.substring(transactionDate.length() - 4);

                // Check if the month and year match
                if (myMonth.equals(transactionMonth) && myYear.equals(transactionYear)) {
                    // Add the amount to the list
                    total += transaction.getAmount();
                }
            }
        }

        return total;
    }*/
/*
    public double getRent(String accountNum) {
        List<Transactions> transactions = repo2.findBudget(accountNum);
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        double total = 0;*/


 /*       public void adminTransaction (String a, String b,double c, String d, String e, String f){
            Transactions transaction = new Transactions();
            transaction.setFromSource(a);
            transaction.setToSource(b);
            transaction.setAmount(c);
            transaction.setTransaction_date();
            transaction.setRefNum();
            transaction.setTypeOf(d);
            transaction.setCategory(e);
            transaction.setPlusorminus(f);
            repo2.save(transaction);
        }*/

    private static final String ZILLOW_API_KEY = "YOUR_ZILLOW_API_KEY";

    public static String zillow(String zillowLink) {

        try {
            String zillowId = extractZillowId(zillowLink);
            if (zillowId != null) {
                return fetchPrice(zillowId);
            } else {
                return "Failed to extract Zillow ID from the link.";
            }
        } catch (IOException e) {

        }
        return "";
    }



    private static String fetchPrice(String zillowId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "http://www.zillow.com/webservice/GetZestimate.htm?zws-id=" + ZILLOW_API_KEY + "&zpid=" + zillowId;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody).getJSONObject("zestimate");
                return jsonObject.getJSONObject("amount").getString("content");
            } else {
                throw new IOException("Unexpected response code: " + response);
            }
        }
    }




    private static String extractZillowId(String url) {
        // Regular expression to match Zillow IDs
        Pattern pattern = Pattern.compile("zpid=(\\d+)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    // Method to send OTP
    public void sendOTPEmail(String to) {
        User user = repo.findByEmail(to);
        String otp = generateOTP();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP for login");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
        user.setOtp(otp);
        repo.save(user);
    }

    // Method to generate OTP
    public String generateOTP() {
        // Simple 6-digit OTP generation
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }

    // Method to validate OTP
//    public boolean validateOTP(String to , String inputOtp) {
//        User user = repo.findByEmail(to);
//        String storedOtp = user.getOtp();
//        return inputOtp.equals(storedOtp);
//    }








}