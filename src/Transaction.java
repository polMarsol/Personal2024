
import java.io.*;
import java.time.LocalDate;

public class Transaction implements Serializable {
    private double amount;
    private String label;
    private LocalDate date;
    private boolean isIncome;
    private String currency; // New field
    public Transaction(double amount, String label, LocalDate date, boolean isIncome) {
        this.amount = amount;
        this.label = label;
        this.date = date;
        this.isIncome = isIncome;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public byte[] toBytes() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(this);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error converting transaction to bytes", e);
        }
    }

    public static Transaction fromBytes(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return (Transaction) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
        return "Quantitat: " + amount + ", Etiqueta: " + label + ", Data: " + date + (isIncome ? ", Ingrés" : ", Despesa") + ", Total: " + getAmount();
    }

}
