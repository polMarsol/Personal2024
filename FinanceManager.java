import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinanceManager {
    private List<Transaction> transactions;
    private String filePath;
    private double totalAmount;
    private int transactionCount = 0;
    private String transactionsFilePath; // New variable
    private String transactionCountFilePath; // New variable
    private String totalExpensesFilePath; // New variable

    public FinanceManager(String filePath) {
        this.transactions = new ArrayList<>();
        this.filePath = filePath;
        this.totalAmount = 0.0;
        this.transactionsFilePath = "transactions.txt"; // Initialize the new variable
        this.transactionCountFilePath = "transactionCount.txt"; // Initialize the new variable
        this.totalExpensesFilePath = "totalExpenses.txt"; // Initialize the new variable
        loadTransactions();
        loadTransactionCount(); // Carga el conteo de transacciones al iniciar el programa
        loadTotalAmount();
    }
    public void saveTransactionCount() {
        try (PrintWriter writer = new PrintWriter(new File("transactionCount.txt"))) {
            writer.println(transactionCount);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void resetData() {
        // Clear the transactions
        transactions.clear();

        // Reset the transaction count and total expenses
        try {
            PrintWriter transactionsWriter = new PrintWriter(transactionsFilePath);
            PrintWriter transactionCountWriter = new PrintWriter(transactionCountFilePath);
            PrintWriter totalExpensesWriter = new PrintWriter("despesa.txt");

            transactionsWriter.print(""); // Borrar todo el contenido del archivo de transacciones
            transactionCountWriter.print("0"); // Establecer el conteo de transacciones a 0
            totalExpensesWriter.print("Total Amount: 0"); // Establecer el total de gastos a 0

            transactionsWriter.close();
            transactionCountWriter.close();
            totalExpensesWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadTransactionCount() {
        File file = new File("transactionCount.txt");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    try (PrintWriter writer = new PrintWriter(file)) {
                        writer.println("0"); // Inicializa transactionCount a 0
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                transactionCount = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveTransactions() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            Transaction transaction = transactions.get(transactions.size() - 1);
            writer.println(transactionCount + " Quantitat: " + transaction.getAmount() + ", Etiqueta: " + transaction.getLabel() + ", Data: " + transaction.getDate() + ", " + (transaction.isIncome() ? "Ingrés" : "Despesa"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTotalAmount() {
        try (PrintWriter writer = new PrintWriter(new File("despesa.txt"))) {
            writer.println("Total Amount: " + totalAmount);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transactionCount++;
        if (transaction.isIncome()) {
            totalAmount += transaction.getAmount();
        } else {
            totalAmount -= transaction.getAmount();
        }
        saveTransactionCount(); // Guarda el conteo de transacciones después de agregar una nueva
    }
    public void loadTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            transactionCount = 0; // reset transactionCount
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length < 5) {
                    continue;
                }
                transactionCount++; // increment transactionCount for each transaction read
                double amount = Double.parseDouble(parts[1].split(": ")[1]);
                String label = parts[2].split(": ")[1];
                LocalDate date = LocalDate.parse(parts[3].split(": ")[1]);
                boolean isIncome = parts[4].trim().equalsIgnoreCase("Ingrés");
                transactions.add(new Transaction(amount, label, date, isIncome));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loadTotalAmount() {
        try (BufferedReader reader = new BufferedReader(new FileReader("despesa.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                totalAmount = Double.parseDouble(line.split(": ")[1]);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    }