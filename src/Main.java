
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
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
        if (!manager.loadTotalAmount()) {
            String salaryInput = JOptionPane.showInputDialog("Introduce tu sueldo global:");
            double salary = Double.parseDouble(salaryInput);
            manager.setTotalAmount(salary);
            manager.saveTotalAmount();
        }
        TransactionForm form = new TransactionForm(manager);

        JFrame frame = new JFrame("My Own Finance");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Margen alrededor de los componentes

        JButton addButton = new JButton("Añadir transacción");
        JButton totalButton = new JButton("Total: " + manager.getTotalAmount());
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                form.setVisible(true);
                Transaction newTransaction = form.getTransaction();
                if (newTransaction != null) {
                    newTransaction.setCurrency((String) form.getCurrencyComboBox().getSelectedItem()); // Set the currency of the transaction
                    manager.addTransaction(newTransaction);
                    totalButton.setText("Total: " + manager.getTotalAmount());
                }
            }
        });
        // Crear un Timer que se dispare cada segundo (1000 milisegundos)
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Actualizar el texto del botón totalButton con el total de dinero actual
                totalButton.setText("Total: " + manager.getTotalAmount());
            }
        });

// Iniciar el Timer
        timer.start();

        JButton sortButton = new JButton("Ordenar per");

        JButton exitButton = new JButton("Sortir");
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
                int response = JOptionPane.showConfirmDialog(null, "Estàs segur que vols resetejar tots les dades?", "Confirmar reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    manager.resetData();
                }
            }
        });

        JButton viewButton = new JButton("Veure transaccions");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Transaction> allTransactions = manager.getTransactions();

                JFrame viewFrame = new JFrame("Totes las transaccionss");
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

        JCheckBox dateCheckBox = new JCheckBox("Data");
        JCheckBox typeCheckBox = new JCheckBox("Tipus");

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

                JFrame sortedFrame = new JFrame("Transaccions ordenades");
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

        JButton statsButton = new JButton("Estadísticas de gasto");
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Diario", "Semanal", "Mensual", "Cancelar"};
                int response = JOptionPane.showOptionDialog(null, "Selecciona el tipo de estadística de gasto que deseas ver:",
                        "Estadísticas de gasto", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                switch (response) {
                    case 0: // Diario
                        JOptionPane.showMessageDialog(null, "Gasto promedio diario: " + manager.getAverageDailySpending() + "\nGanancia promedio diaria: " + manager.getAverageDailyIncome());
                        break;
                    case 1: // Semanal
                        JOptionPane.showMessageDialog(null, "Gasto promedio semanal: " + manager.getAverageWeeklySpending() + "\nGanancia promedio semanal: " + manager.getAverageWeeklyIncome());
                        break;
                    case 2: // Mensual
                        JOptionPane.showMessageDialog(null, "Gasto promedio mensual: " + manager.getAverageMonthlySpending() + "\nGanancia promedio mensual: " + manager.getAverageMonthlyIncome());
                        break;
                    case 3: // Cancelar
                        // No hacer nada, simplemente cerrar el cuadro de diálogo
                        break;
                }
            }
        });
        JPopupMenu popupMenu = new JPopupMenu();

// Crear el elemento "Salir" y añadirlo al menú
        JMenuItem exitMenuItem = new JMenuItem("Salir");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        popupMenu.add(exitMenuItem);

// Crear el elemento "Reset" y añadirlo al menú
        JMenuItem resetMenuItem = new JMenuItem("Reset");
        resetMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Estàs segur que vols resetejar tots les dades?", "Confirmar reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    manager.resetData();
                }
            }
        });
        popupMenu.add(resetMenuItem);

// Crear un botón que muestre el menú desplegable cuando se haga clic en él
        JButton menuButton = new JButton("Menú");
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenu.show(menuButton, menuButton.getWidth() / 2, menuButton.getHeight() / 2);
            }
        });

// Añadir el botón al panel
        gbc.gridx = 1;
        gbc.gridy = 0; // Ajusta estos valores según donde quieras colocar el botón en tu panel
        panel.add(menuButton, gbc);

// Añade el botón statsButton a tu panel
        gbc.gridx = 0;
        gbc.gridy = 3; // Ajusta este valor según donde quieras colocar el botón en tu panel
        panel.add(statsButton, gbc);

// First row
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(viewButton, gbc);

// Second row
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(sortButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(totalButton, gbc);

// Third row
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(dateCheckBox, gbc);

        gbc.gridx = 1;
        panel.add(typeCheckBox, gbc);

// Fourth row
/*        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(resetButton, gbc);

        gbc.gridx = 1;
        panel.add(exitButton, gbc);*/

        frame.add(panel);
        frame.setVisible(true);

    }
}
