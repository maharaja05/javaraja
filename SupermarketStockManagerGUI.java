import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

 class SupermarketStockManagerGUI extends JFrame {
    private Map<String, Integer> stock;
    private JTextArea stockTextArea;

    public SupermarketStockManagerGUI() {
        super("Supermarket Stock Manager");
        stock = new HashMap<>();

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application on window close
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        stockTextArea = new JTextArea(10, 30);
        stockTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(stockTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton viewButton = new JButton("View Stock");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        viewStock();
                    }
                });
            }
        });
        buttonPanel.add(viewButton);

        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        addItem();
                    }
                });
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Quantity");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateQuantity();
                    }
                });
            }
        });
        buttonPanel.add(updateButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);
    }

    private void viewStock() {
        if (stock.isEmpty()) {
            stockTextArea.setText("Stock is empty.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Stock:\n");
            for (Map.Entry<String, Integer> entry : stock.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            stockTextArea.setText(sb.toString());
        }
    }

    private void addItem() {
        String itemName = JOptionPane.showInputDialog(this, "Enter item name:");
        if (itemName != null && !itemName.trim().isEmpty()) {
            String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity:");
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity >= 0) {
                        stock.put(itemName, quantity);
                        JOptionPane.showMessageDialog(this, itemName + " added to stock with quantity " + quantity);
                    } else {
                        JOptionPane.showMessageDialog(this, "Quantity must be a non-negative number.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a number.");
                }
            }
        }
    }

    private void updateQuantity() {
        String itemName = JOptionPane.showInputDialog(this, "Enter item name to update quantity:");
        if (itemName != null && stock.containsKey(itemName)) {
            String newQuantityStr = JOptionPane.showInputDialog(this, "Enter new quantity:");
            if (newQuantityStr != null && !newQuantityStr.trim().isEmpty()) {
                try {
                    int newQuantity = Integer.parseInt(newQuantityStr);
                    if (newQuantity >= 0) {
                        stock.put(itemName, newQuantity);
                        JOptionPane.showMessageDialog(this, itemName + " quantity updated to " + newQuantity);
                    } else {
                        JOptionPane.showMessageDialog(this, "Quantity must be a non-negative number.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a number.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Item not found in stock.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SupermarketStockManagerGUI();
            }
        });
    }
}