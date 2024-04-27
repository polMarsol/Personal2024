import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        FinanceManager manager = new FinanceManager("transactions.txt");
        TransactionForm form = new TransactionForm(manager);

        JFrame frame = new JFrame("Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JButton addButton = new JButton("Añadir transacción");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                form.setVisible(true);
            }
        });

        JButton exitButton = new JButton("Salir");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(exitButton);
        frame.add(panel);

        frame.setVisible(true);
    }
}