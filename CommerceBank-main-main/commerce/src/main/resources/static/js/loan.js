function calculateMortgage() {
    var homePrice = parseFloat(document.getElementById("homePrice").value);
    var downPayment = parseFloat(document.getElementById("downPayment").value);
    var loanTerm = parseInt(document.getElementById("loanTerm").value);
    var interestRate = parseFloat(document.getElementById("interestRate").value);
    console.log("Calculating mortgage...");


    // Check if any input value is NaN (not a number)
    if (isNaN(homePrice) || isNaN(downPayment) || isNaN(loanTerm) || isNaN(interestRate)) {
        alert("Please enter valid numeric values for all input fields.");
        return; // Exit the function if any input is invalid
    }

    var principal = homePrice - downPayment;
    var monthlyInterestRate = (interestRate / 100) / 12;
    var numberOfPayments = loanTerm * 12;
    var monthlyPayment = principal * monthlyInterestRate /
            (1 - Math.pow(1 + monthlyInterestRate, -numberOfPayments));

    var formattedMonthlyPayment = monthlyPayment.toFixed(2);

    // Display the mortgage loan details in the result div
    var mortgageResult = document.getElementById("mortgageResult");
    mortgageResult.innerHTML = "<h2>Mortgage Loan Details</h2>" +
                               "<p>Principal: $" + principal.toFixed(2) + "</p>" +
                               "<p>Monthly Payment: $" + formattedMonthlyPayment + "</p>";
}

function calculateStudentLoan() {
    var loanAmount = parseFloat(document.getElementById("loanAmount").value);
    var loanTermYears = parseInt(document.getElementById("loanTermYears").value);
    var interestRate = parseFloat(document.getElementById("interestRate2").value);

    // Check if any input value is NaN (not a number)
    if (isNaN(loanAmount) || isNaN(loanTermYears) || isNaN(interestRate)) {
        alert("Please enter valid numeric values for all input fields.");
        return; // Exit the function if any input is invalid
    }

    var monthlyInterestRate = interestRate / 100 / 12;
    var numberOfPayments = loanTermYears * 12;

    // Calculate monthly payment using the formula for amortizing a loan
    var monthlyPayment = loanAmount * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -numberOfPayments));

    // Display the student loan details in the result div
    var studentLoanResult = document.getElementById("studentLoanResult");
    studentLoanResult.innerHTML = "<h2>Student Loan Details</h2>" +
                                   "<p>Loan Amount: $" + loanAmount.toFixed(2) + "</p>" +
                                   "<p>Loan Term (years): " + loanTermYears + "</p>" +
                                   "<p>Interest Rate: " + interestRate + "%</p>" +
                                   "<p>Monthly Payment: $" + monthlyPayment.toFixed(2) + "</p>";
}

function calculateAutoLoan() {
    var loanAmount = parseFloat(document.getElementById("loanAmountAuto").value);
    var loanTermYears = parseInt(document.getElementById("loanTermYearsAuto").value);
    var interestRate = parseFloat(document.getElementById("interestRateAuto").value);
    var tradeInValue = parseFloat(document.getElementById("tradeInValue").value);
    var downPayment = parseFloat(document.getElementById("downPaymentAuto").value);
    var salesTaxRate = parseFloat(document.getElementById("salesTaxRate").value);
    var fees = parseFloat(document.getElementById("fees").value);

    // Check if any input value is NaN (not a number)
    if (isNaN(loanAmount) || isNaN(loanTermYears) || isNaN(interestRate) ||
        isNaN(tradeInValue) || isNaN(downPayment) || isNaN(salesTaxRate) || isNaN(fees)) {
        alert("Please enter valid numeric values for all input fields.");
        return; // Exit the function if any input is invalid
    }

    // Calculate the total loan amount including sales tax and fees
    var totalLoanAmount = loanAmount - tradeInValue - downPayment + (loanAmount * salesTaxRate / 100) + fees;

    var monthlyInterestRate = interestRate / 100 / 12;
    var numberOfPayments = loanTermYears * 12;

    // Calculate monthly payment using the formula for amortizing a loan
    var monthlyPayment = totalLoanAmount * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -numberOfPayments));

    // Display the auto loan details in the result div
    var autoLoanResult = document.getElementById("autoLoanResult");
    autoLoanResult.innerHTML = "<h2>Auto Loan Details</h2>" +
                               "<p>Total Loan Amount: $" + totalLoanAmount.toFixed(2) + "</p>" +
                               "<p>Loan Term (years): " + loanTermYears + "</p>" +
                               "<p>Interest Rate: " + interestRate + "%</p>" +
                               "<p>Monthly Payment: $" + monthlyPayment.toFixed(2) + "</p>";
}
