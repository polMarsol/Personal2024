import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FinanceManager manager = new FinanceManager("transactions.db");
        TransactionForm form = new TransactionForm(manager);

        JFrame frame = new JFrame("Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel(); // Declare and initialize panel here

        JButton addButton = new JButton("Añadir transacción");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                form.setVisible(true);
            }
        });

        JButton sortButton = new JButton("Ordenar por");

        JButton exitButton = new JButton("Salir");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.resetData();
            }
        });

        JButton viewButton = new JButton("Ver transacciones");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Transaction> allTransactions = manager.getTransactions();

                JFrame viewFrame = new JFrame("Todas las transacciones");
                viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                viewFrame.setSize(400, 200);

                JTextArea textArea = new JTextArea();
                for (Transaction transaction : allTransactions) {
                    textArea.append(transaction.toString() + "\n");
                }

                viewFrame.add(new JScrollPane(textArea));
                viewFrame.setVisible(true);
            }
        });

        JCheckBox dateCheckBox = new JCheckBox("Fecha");
        JCheckBox typeCheckBox = new JCheckBox("Tipo");

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Transaction> sortedTransactions = new ArrayList<>(manager.getTransactions());

                if (typeCheckBox.isSelected() && dateCheckBox.isSelected()) {
                    sortedTransactions.sort(Comparator.comparing(Transaction::isIncome)
                            .thenComparing(Transaction::getDate));
                } else if (dateCheckBox.isSelected()) {
                    sortedTransactions.sort(Comparator.comparing(Transaction::getDate));
                } else if (typeCheckBox.isSelected()) {
                    sortedTransactions.sort(Comparator.comparing(Transaction::isIncome));
                }

                JFrame sortedFrame = new JFrame("Transacciones ordenadas");
                sortedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                sortedFrame.setSize(400, 200);

                JTextArea textArea = new JTextArea();
                for (Transaction transaction : sortedTransactions) {
                    textArea.append(transaction.toString() + "\n");
                }

                sortedFrame.add(new JScrollPane(textArea));
                sortedFrame.setVisible(true);
            }
        });

        panel.add(addButton);
        panel.add(sortButton);
        panel.add(exitButton);
        panel.add(resetButton);
        panel.add(viewButton);
        panel.add(dateCheckBox);
        panel.add(typeCheckBox);
        frame.add(panel);

        frame.setVisible(true);
    }
}