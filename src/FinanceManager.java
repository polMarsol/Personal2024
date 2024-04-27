import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FinanceManager {
    private List<Transaction> transactions;
    private String filePath = "transactions.db";
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
    public List<Transaction> sortByDate() {
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        sortedTransactions.sort(Comparator.comparing(Transaction::getDate));
        return sortedTransactions;
    }

    public List<Transaction> sortByType() {
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        sortedTransactions.sort(Comparator.comparing(Transaction::isIncome));
        return sortedTransactions;
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
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            for (Transaction transaction : transactions) {
                byte[] bytes = transaction.toBytes();
                raf.writeInt(bytes.length);
                raf.write(bytes);
            }
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
        transactions = new ArrayList<>();
        File file = new File("transactions.db");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int length = raf.readInt();
                byte[] bytes = new byte[length];
                raf.readFully(bytes);
                Transaction transaction = Transaction.fromBytes(bytes);
                if (transaction != null) {
                    transactions.add(transaction);
                }
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