import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionForm extends JFrame {
    private JTextField amountField;
    private JTextField labelField;
    private JTextField dateField;
    private JCheckBox isIncomeCheckBox;
    private JButton submitButton;
    private JButton cancelButton;
    private JButton resetButton; // Nuevo botón de reset
    private FinanceManager manager;

    public TransactionForm(FinanceManager manager) {
        this.manager = manager;

        setLayout(new GridLayout(7, 2)); // Modificado para incluir el nuevo botón

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

                Transaction transaction = new Transaction(amount, label, date, isIncome);
                manager.addTransaction(transaction);
                manager.saveTransactions();
                manager.saveTotalAmount();

                amountField.setText("");
                labelField.setText("");
                dateField.setText("");
                isIncomeCheckBox.setSelected(false);

                setVisible(false);

                // Cerrar el programa actual
                dispose();
            }
        });
        add(submitButton);

        cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                amountField.setText("");
                labelField.setText("");
                dateField.setText("");
                isIncomeCheckBox.setSelected(false);

                setVisible(false);
            }
        });
        add(cancelButton);

        // Nuevo botón de reset
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.resetData();
                amountField.setText("");
                labelField.setText("");
                dateField.setText("");
                isIncomeCheckBox.setSelected(false);

                // Cerrar el programa actual
                System.exit(0);

                // Iniciar una nueva instancia del programa
                // Nota: Reemplaza "Main" con el nombre de tu clase principal
                String[] cmd = {"java", "Main"};
                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(resetButton);

        pack();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
}