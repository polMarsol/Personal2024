import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Dimension buttonSize = new Dimension(100, 50);
        try {
            // Establecer la apariencia de los componentes de la interfaz de usuario en Metal
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FinanceManager manager = new FinanceManager("transactions.db");
        TransactionForm form = new TransactionForm(manager);

        JFrame frame = new JFrame("Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Margen alrededor de los componentes


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


// First row
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(addButton, gbc);

        gbc.gridx = 1;
        panel.add(viewButton, gbc);

// Second row
        gbc.gridx = 1/2;
        gbc.gridy = 1;
        panel.add(sortButton, gbc);

// Third row
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(dateCheckBox, gbc);

        gbc.gridx = 1;
        panel.add(typeCheckBox, gbc);

// Fourth row
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(resetButton, gbc);

        gbc.gridx = 1;
        panel.add(exitButton, gbc);

        frame.add(panel);

        frame.setVisible(true);
    }
}