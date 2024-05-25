package com.Commerceapp.app.rest;

//import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import java.util.*;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AppController {

	@Autowired
	private UserRepository repo;

	@Autowired
	private TransactionsRepository repository;

	@Autowired
	private CustomUserDetailsService userService;


	@GetMapping("/")
	public String viewHomePage(@RequestParam(value = "error", required = false) String error,
							   @RequestParam(value = "logout", required = false) String logout, Model model) {
		model.addAttribute("user", new User());
		if (error != null) {
			model.addAttribute("error", "Your username and password are invalid.");
			return "login-failed";
		}
		if (logout != null) {
			model.addAttribute("message", "You have been logged out successfully.");
		}
		return "index";

	}

	@GetMapping("/budget")
	public String viewBudget(Model model, @AuthenticationPrincipal UserDetails currentUser) {
		User user = repo.findByEmail(currentUser.getUsername());
		model.addAttribute("user", user);
		double monthly_income = userService.getMatchingAmounts(user.getAccountNum());
		double[] pie_nums = userService.pie(monthly_income);

		double rent1 = userService.getRent(user.getAccountNum());
		double bills1 = userService.getBills(user.getAccountNum());
		double misc1 = userService.getMisc(user.getAccountNum());
		double transport1 = userService.getTransport(user.getAccountNum());
		double food1 = userService.getFood(user.getAccountNum());
		double result = rent1 + bills1 + misc1 + transport1 + food1;
		double rawsavings = monthly_income - result;
		double savings1 = userService.decimal(rawsavings);

		double[] pie2 = userService.pie2(pie_nums[0], rent1, pie_nums[1], bills1, pie_nums[2], transport1, pie_nums[3], food1, pie_nums[4], misc1, pie_nums[5], savings1);
		String[] pie3 = userService.pie3(pie2);




		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM");



		model.addAttribute("income", monthly_income);
		model.addAttribute("rent", pie_nums[0]);
		model.addAttribute("bills", pie_nums[1]);
		model.addAttribute("transport", pie_nums[2]);
		model.addAttribute("food", pie_nums[3]);
		model.addAttribute("misc", pie_nums[4]);
		model.addAttribute("savings", pie_nums[5]);
		model.addAttribute("month", formatter.format(java.time.LocalDate.now()));


		model.addAttribute("rent1", rent1);
		model.addAttribute("bills1", bills1);
		model.addAttribute("transport1", transport1);
		model.addAttribute("food1", food1);
		model.addAttribute("misc1", misc1);
		model.addAttribute("savings1", savings1);

		model.addAttribute("rentdif", Math.abs(pie2[0]));
		model.addAttribute("billsdif", Math.abs(pie2[1]));
		model.addAttribute("transportdif", Math.abs(pie2[2]));
		model.addAttribute("fooddif", Math.abs(pie2[3]));
		model.addAttribute("miscdif", Math.abs(pie2[4]));
		model.addAttribute("savingsdif", Math.abs(pie2[5]));

		model.addAttribute("rentdif1", pie3[0]);
		model.addAttribute("billsdif1", pie3[1]);
		model.addAttribute("transportdif1", pie3[2]);
		model.addAttribute("fooddif1", pie3[3]);
		model.addAttribute("miscdif1", pie3[4]);
		model.addAttribute("savingsdif1", pie3[5]);

		return "budgeting";
	}



	@GetMapping("/register")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());

		return "signup_form";
	}

	@GetMapping("/2fa")
	public String twoFactor(Model model, @AuthenticationPrincipal UserDetails currentUser) {
		userService.sendOTPEmail(currentUser.getUsername());
		return "2fa";
	}

	@PostMapping("/forgot_pass")
	public String forgotPass(@RequestParam("email") String email,
							 @RequestParam("newpassword") String newpassword,
							 Model model,
							 User user){


		return "";
	}



	@GetMapping("/activate")
	public String showactivateform(Model model) {
		model.addAttribute("user", new User());
		return "activate_form";
	}

	@GetMapping("/login")
	public String showSignInForm(@RequestParam(value = "error", required = false) String error,
								 @RequestParam(value = "logout", required = false) String logout, Model model) {
		model.addAttribute("user", new User());
		if (error != null) {
			model.addAttribute("error", "Your username and password are invalid.");
			return "login-failed";
		}
		if (logout != null) {
			model.addAttribute("message", "You have been logged out successfully.");
		}
		return "loginpage";
	}

	@PostMapping("/process_register")
	public String processRegistration(User user, RedirectAttributes redirectAttributes) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setAccountNum();
		repo.save(user);

		return "register_success";

	}

	@GetMapping("/activate_error")
	public String activate_error(Model model) {
		model.addAttribute("error", "Activation failed. Account doesn't exist or other error occurred.");
		return "activate_error1";
	}

	@PostMapping("/process_activate")
	public String processActivate(@RequestParam("accountNum") String accountNum,
								  @RequestParam("firstName") String firstName,
								  @RequestParam("lastName") String lastName,
								  @RequestParam("email") String email,
								  @RequestParam("password") String password,
								  Model model,
								  User user,
								  RedirectAttributes redirectAttributes) {
		User toUser = repo.findByAccountNum(accountNum).orElse(null);
		if(toUser != null){
			if(Objects.equals(toUser.getFirstName(), firstName) && Objects.equals(toUser.getLastName(), lastName)) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				String encodedPassword = encoder.encode(password);
				toUser.setPassword(encodedPassword);
				toUser.setEmail(email);
				repo.save(toUser);
				return "register_success";
			} else {
				return "activate_error2";
			}}
		else {
			redirectAttributes.addFlashAttribute("error", "Activation failed. Account doesn't exist.");
			return "activate_error1";

		}
	}

	@GetMapping("/settings")
	public String showUserSettings(Model model, @AuthenticationPrincipal UserDetails currentUser) {
		// Assuming you have a method to get the updated user details
		User user = repo.findByEmail(currentUser.getUsername());
		model.addAttribute("user", user);
		double monthly_income = userService.getMatchingAmounts(user.getAccountNum());

		String fullName = user.getFirstName() + " " + user.getLastName();
		String address = user.getAddress();
		double user_balance = user.getBalance();
		BigDecimal bankbalance = BigDecimal.valueOf(user_balance).setScale(2, RoundingMode.HALF_UP);

		model.addAttribute("fullName", fullName);
		model.addAttribute("dob", user.getDob());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("address", address);
		model.addAttribute("accountNum", user.getAccountNum());
		model.addAttribute("balance", bankbalance);
		model.addAttribute("income", monthly_income);


		return "user_settings";
	}

	@GetMapping("/settings_error")
	public String showUserSettingsError(Model model, @AuthenticationPrincipal UserDetails currentUser) {
		// Assuming you have a method to get the updated user details
		User user = repo.findByEmail(currentUser.getUsername());
		model.addAttribute("user", user);
		double monthly_income = userService.getMatchingAmounts(user.getAccountNum());

		String fullName = user.getFirstName() + " " + user.getLastName();
		String address = user.getAddress();
		double user_balance = user.getBalance();
		BigDecimal bankbalance = BigDecimal.valueOf(user_balance).setScale(2, RoundingMode.HALF_UP);

		model.addAttribute("fullName", fullName);
		model.addAttribute("dob", user.getDob());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("address", address);
		model.addAttribute("accountNum", user.getAccountNum());
		model.addAttribute("balance", bankbalance);
		model.addAttribute("income", monthly_income);


		return "user_settings_error";
	}

	@PostMapping("/process_change")
	public String processChange(@RequestParam("accountNum") String accountNum,
								@RequestParam("firstName") String firstName,
								@RequestParam("lastName") String lastName,
								@RequestParam("email") String email,
								@RequestParam("password") String password,
								@RequestParam("newPassword") String newPassword,
								@RequestParam("phoneNum") String phoneNum,
								@RequestParam("address") String address,
								Model model,
								User user) {
		User toUser = repo.findByAccountNum(accountNum).orElse(null);
		boolean success = userService.quickCheck2(accountNum, password);
		if(toUser != null) {
			if (success) {
				if (newPassword.length() >= 6) {
					BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
					String encodedPassword = encoder.encode(newPassword);
					toUser.setPassword(encodedPassword);
				}
				toUser.setEmail(email);
				toUser.setPhoneNum(phoneNum);
				toUser.setFirstName(firstName);
				toUser.setLastName(lastName);
				toUser.setAddress(address);
				repo.save(toUser);
			}
			else{
				return "redirect:/settings_error";
			}

		}
		return "redirect:/settings";
	}

	@GetMapping("/user__settings")
	public String refreshUserInfoSettings() {
		return "redirect:/settings";
	}




	@GetMapping("/dashboard")
	public String showDash(Model model, @AuthenticationPrincipal UserDetails currentUser) {
		// Assuming you have a method to get the updated user details
		User user = repo.findByEmail(currentUser.getUsername());
		List<Transactions> transactions = repository.findByFromSource(user.getAccountNum());
		List<Transactions> transactionsCopy = new ArrayList<>(transactions); // Shallow copy
		Collections.reverse(transactionsCopy);
		model.addAttribute("transactions", transactions);
		model.addAttribute("transactions2", transactionsCopy);
		double monthly_income = userService.getMatchingAmounts(user.getAccountNum());

		String fullName = user.getFirstName() + " " + user.getLastName();
		String address = user.getAddress();
		double user_balance = user.getBalance();
		BigDecimal bankbalance = BigDecimal.valueOf(user_balance).setScale(2, RoundingMode.HALF_UP);

		model.addAttribute("fullName", fullName);
		model.addAttribute("dob", user.getDob());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("address", address);
		model.addAttribute("accountNum", user.getAccountNum());
		model.addAttribute("balance", bankbalance);
		model.addAttribute("income", monthly_income);



//		model.addAttribute("tosource","******" + transaction.getToSource().substring(6));
//		model.addAttribute("amount", transaction.getAmount());
//		model.addAttribute("date", transaction.getTransaction_date());






		return "dashboard";

	}

	/*@PostMapping("/transfer-form")
	public String showTransferForm(User user, Model model, TransferRequest transferRequest, RedirectAttributes redirectAttributes) {
		boolean success = userService.transferAmount(transferRequest.getFromAccountNum(), transferRequest.getToAccountNum(), transferRequest.getAmount());
		if (success) {
			redirectAttributes.addFlashAttribute("message", "Transfer Successful!");
			return "transfer_success";
		} else {
			redirectAttributes.addFlashAttribute("message", "Transfer failed");
			return "transfer_failed";
		}
	}*/

	/*@GetMapping("/transfer")
	public String showTransfer(Model model) {
		model.addAttribute("transferRequest", new TransferRequest());
		return "transferForm";
	}*/

	@GetMapping("/transfer")
	public String transfertest(Model model) {
		model.addAttribute("transferRequest", new TransferRequest());
		return "transferForm1";

	}

	@PostMapping("/transfer-form")
	public String showTransferForm(User user, Model model, TransferRequest transferRequest, RedirectAttributes redirectAttributes,  @AuthenticationPrincipal UserDetails currentUser) {
		boolean success = userService.quickCheck(currentUser.getUsername(), transferRequest.getFromEmail(), transferRequest.getFromPassword());
		if (success) {
			boolean success2 = userService.transferAmounts(transferRequest.getFromEmail(), transferRequest.getToAccountNum(), transferRequest.getAmount());
			if (success2) {
//				redirectAttributes.addFlashAttribute("message", "Transfer Successful!");
				userService.newTransaction(transferRequest.getFromEmail(), transferRequest.getToAccountNum(), transferRequest.getAmount());
				return "transfer_success";
			} else {
//				redirectAttributes.addFlashAttribute("message", "Transfer failed");
				return "transfer_failed";
			}
		}
		else {
			return "transfer_failed2";
		}
	}

	@GetMapping("/dash_board")
	public String refreshUserInfo() {
		return "redirect:/dashboard";
	}

	@GetMapping("/budgeting")
	public String viewbudget() {
		return "budgeting";

	}

	@GetMapping("/loan")
	public String loantest() {
		return "loan";

	}



	public static class TransferRequest {
		private String fromEmail;
		private String fromPassword;
		private String fromAccountNum;
		private String toAccountNum;
		private double amount;

		public String getFromEmail() {
			return fromEmail;
		}
		public void setFromEmail(String fromEmail) {
			this.fromEmail = fromEmail;
		}
		public String getFromPassword() {
			return fromPassword;
		}
		public void setFromPassword(String fromPassword) {
			this.fromPassword = fromPassword;
		}
		public String getFromAccountNum() {
			return fromAccountNum;
		}
		public void setFromAccountNum(String fromAccountNum) {
			this.fromAccountNum = fromAccountNum;
		}
		public String getToAccountNum() {
			return toAccountNum;
		}
		public void setToAccountNum(String toAccountNum) {
			this.toAccountNum = toAccountNum;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}


	}

	@PostMapping("/forgot")
	public String sendOtpEmail(@RequestParam("inputOtp") String inputOtp, @AuthenticationPrincipal UserDetails currentUser) {
		User user = repo.findByEmail(currentUser.getUsername());
		String storedOtp = user.getOtp();
		if(Objects.equals(storedOtp, inputOtp)){
			return "redirect:/dashboard";
		}
		else {
			return "2fa_error";
		}
	}

	@PostMapping("/forgot_2fa")
	public String sendOtpEmail2(@RequestParam("email") String email) {

		User user = repo.findByEmail(email);
		if (user != null){
			userService.sendOTPEmail(email);
			return "2fa_forgot";
		}
		else {
			return "error";
		}
	}



	@GetMapping("/2fa_forgot")
	public String twoFactor2(@RequestParam("email") String email, Model model) {
		model.addAttribute("email", email);
		return "2fa_forgot";
	}

	@PostMapping("/forgot_2fa2")
	public String sendOtpEmail3(@RequestParam("email") String email, @RequestParam("inputOtp") String inputOtp) {

		User user = repo.findByEmail(email);
		String storedOtp = user.getOtp();
		if(Objects.equals(storedOtp, inputOtp)){
			return "newpassword";
		}
		else {
			return "error";
		}
	}

	@PostMapping("/new_pass")
	public String newPass(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword) {

		User user = repo.findByEmail(email);
		if (newPassword != null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(newPassword);
			user.setPassword(encodedPassword);
			repo.save(user);
		}
		return "register_success";
	}

	@GetMapping("/forgot_pass")
	public String forgotPass(Model model) {
		return "forgot_pass";
	}






}