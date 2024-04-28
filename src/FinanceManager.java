
import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

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
        transactions.clear();
        transactionCount = 0;
        totalAmount = 0;
        saveTransactionCount();
        saveTotalAmount();

        // Borra el contenido del archivo de base de datos
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            raf.setLength(0); // Establece la longitud del archivo a 0, borrando todo su contenido
        } catch (IOException e) {
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
        saveTotalAmount(); // Guarda el sueldo global después de agregar una nueva transacción
        loadTotalAmount();
    }
    public void loadTransactions() {
        transactions = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int size = raf.readInt(); // Read the size of the next Transaction object
                byte[] data = new byte[size]; // Create a byte array of the correct size
                raf.readFully(data); // Read the Transaction object
                // Deserialize the Transaction object and add it to the list
                Transaction transaction = Transaction.fromBytes(data);
                transactions.add(transaction);
            }
        } catch (EOFException e) {
            // This exception is thrown when the end of the file is reached.
            // In this case, we have read all Transaction objects, so we can just return.
            return;
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
    public double getAverageDailySpending() {
        List<Transaction> transactions = getTransactions();
        double totalSpending = 0.0;
        for (Transaction transaction : transactions) {
            if (!transaction.isIncome()) {
                totalSpending += transaction.getAmount();
            }
        }
        long days = ChronoUnit.DAYS.between(getEarliestTransactionDate(), LocalDate.now()) + 1;
        return totalSpending / days;
    }
    public double getAverageDailyIncome() {
        List<Transaction> transactions = getTransactions();
        double totalIncome = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.isIncome()) {
                totalIncome += transaction.getAmount();
            }
        }
        long days = ChronoUnit.DAYS.between(getEarliestTransactionDate(), LocalDate.now()) + 1;
        return totalIncome / days;
    }
    public double getAverageWeeklyIncome() {
        return getAverageDailyIncome() * 7;
    }

    public double getAverageMonthlyIncome() {
        return getAverageDailyIncome() * 30;
    }
    public double getAverageWeeklySpending() {
        return getAverageDailySpending() * 7;
    }

    public double getAverageMonthlySpending() {
        return getAverageDailySpending() * 30;
    }

    private LocalDate getEarliestTransactionDate() {
        return getTransactions().stream()
                .min(Comparator.comparing(Transaction::getDate))
                .orElseThrow(() -> new NoSuchElementException("No transactions found"))
                .getDate();
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
