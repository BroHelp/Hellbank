import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// ===================== ABSTRACTION =====================
abstract class Account {
    private String accountNumber;
    private double balance;

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // Encapsulation (getters/setters)
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    // Abstract method (polymorphism)
    public abstract void deposit(double amount);
    public abstract boolean withdraw(double amount);
}

// ===================== INHERITANCE =====================
class SavingsAccount extends Account {
    public SavingsAccount(String accNo, double balance) {
        super(accNo, balance);
    }

    @Override
    public void deposit(double amount) {
        setBalance(getBalance() + amount);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= getBalance()) {
            setBalance(getBalance() - amount);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient funds!");
            return false;
        }
    }
}

class CurrentAccount extends Account {
    public CurrentAccount(String accNo, double balance) {
        super(accNo, balance);
    }

    @Override
    public void deposit(double amount) {
        setBalance(getBalance() + amount);
    }

    @Override
    public boolean withdraw(double amount) {
        // Allows overdraft of 500
        if (amount <= getBalance() + 500) {
            setBalance(getBalance() - amount);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Overdraft limit exceeded!");
            return false;
        }
    }
}

// ===================== BANK CLASS =====================
class Bank {
    private Map<String, Account> accounts = new HashMap<>();

    public void addAccount(Account acc) {
        accounts.put(acc.getAccountNumber(), acc);
    }

    public Account getAccount(String accNo) {
        return accounts.get(accNo);
    }
}

// ===================== GUI =====================
public class BankingApp extends JFrame {

    private Bank bank = new Bank();

    private JTextField accField, amountField;
    private JTextArea outputArea;

    public BankingApp() {
        setTitle("Simple Banking App");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        accField = new JTextField(15);
        amountField = new JTextField(10);
        outputArea = new JTextArea(10, 30);

        JButton createSavings = new JButton("Create Savings");
        JButton createCurrent = new JButton("Create Current");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton checkBtn = new JButton("Check Balance");

        add(new JLabel("Account No:"));
        add(accField);
        add(new JLabel("Amount:"));
        add(amountField);

        add(createSavings);
        add(createCurrent);
        add(depositBtn);
        add(withdrawBtn);
        add(checkBtn);

        add(new JScrollPane(outputArea));

        // Actions
        createSavings.addActionListener(e -> {
            String accNo = accField.getText().trim();
            if (!accNo.isEmpty()) {
                if (bank.getAccount(accNo) == null) {
                    bank.addAccount(new SavingsAccount(accNo, 0));
                    outputArea.append("Savings account created: " + accNo + "\n");
                } else {
                    outputArea.append("Account already exists\n");
                }
            } else {
                outputArea.append("Enter account number\n");
            }
        });

        createCurrent.addActionListener(e -> {
            String accNo = accField.getText().trim();
            if (!accNo.isEmpty()) {
                if (bank.getAccount(accNo) == null) {
                    bank.addAccount(new CurrentAccount(accNo, 0));
                    outputArea.append("Current account created: " + accNo + "\n");
                } else {
                    outputArea.append("Account already exists\n");
                }
            } else {
                outputArea.append("Enter account number\n");
            }
        });

        depositBtn.addActionListener(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText().trim());
                Account acc = bank.getAccount(accField.getText().trim());
                if (acc != null) {
                    acc.deposit(amt);
                    outputArea.append("Deposited: " + amt + "\n");
                } else {
                    outputArea.append("Account not found\n");
                }
            } catch (NumberFormatException ex) {
                outputArea.append("Invalid amount\n");
            }
        });

        withdrawBtn.addActionListener(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText().trim());
                Account acc = bank.getAccount(accField.getText().trim());
                if (acc != null) {
                    if (acc.withdraw(amt)) {
                        outputArea.append("Withdrawn: " + amt + "\n");
                    }
                } else {
                    outputArea.append("Account not found\n");
                }
            } catch (NumberFormatException ex) {
                outputArea.append("Invalid amount\n");
            }
        });

        checkBtn.addActionListener(e -> {
            Account acc = bank.getAccount(accField.getText().trim());
            if (acc != null) {
                outputArea.append("Balance: " + acc.getBalance() + "\n");
            } else {
                outputArea.append("Account not found\n");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankingApp().setVisible(true));
    }
}