import java.time.LocalDate;

public class Transaction {
    private double amount;
    private String label;
    private LocalDate date;
    private boolean isIncome;

    public Transaction(double amount, String label, LocalDate date, boolean isIncome) {
        this.amount = amount;
        this.label = label;
        this.date = date;
        this.isIncome = isIncome;
    }

    public double getAmount() {
        return amount;
    }

    public String getLabel() {
        return label;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isIncome() {
        return isIncome;
    }
    @Override
    public String toString() {
        return "Quantitat: " + amount + ", Etiqueta: " + label + ", Data: " + date + (isIncome ? ", Ingrés" : ", Despesa");
    }
}