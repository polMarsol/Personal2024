import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionForm extends JFrame {
    private JTextField amountField;
    private JTextField labelField;
    private JTextField dateField;
    private JCheckBox isIncomeCheckBox;
    private JButton submitButton;
    private FinanceManager manager;

    public TransactionForm(FinanceManager manager) {
        this.manager = manager;

        setLayout(new GridLayout(5, 2));

        add(new JLabel("Quantitat:"));
        amountField = new JTextField();
        add(amountField);

        add(new JLabel("Etiqueta:"));
        labelField = new JTextField();
        add(labelField);

        add(new JLabel("Data (dd/MM/yyyy):"));
        dateField = new JTextField();
        add(dateField);

        add(new JLabel("Ingrés?"));
        isIncomeCheckBox = new JCheckBox();
        add(isIncomeCheckBox);

        submitButton = new JButton("Acceptar");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = Double.parseDouble(amountField.getText());
                String label = labelField.getText();
                LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                boolean isIncome = isIncomeCheckBox.isSelected();

                // Crea una nova transacció
                Transaction transaction = new Transaction(amount, label, date, isIncome);
                manager.addTransaction(transaction);
                manager.saveTransactions();
                manager.saveTotalAmount();

                amountField.setText("");
                labelField.setText("");
                dateField.setText("");
                isIncomeCheckBox.setSelected(false);
            }
        });
        add(submitButton);

        pack();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
}