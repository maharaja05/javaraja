import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SupermarketStockManagerGUI extends JFrame {
    private Map<String, Integer> stock;
    private JTextArea stockTextArea;
    private Connection connection;

    public SupermarketStockManagerGUI() {
        super("Supermarket Stock Manager");
        stock = new HashMap<>();

        // Initialize database connection
        try {
            // Replace with your database details
            String url = "jdbc:mysql://localhost:3306/your_database";
            String username = "root";
            String password = "Marvel@0";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
            System.exit(1); // Exit application on database connection error
        }

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        try {
            // Execute query to fetch all items from database
            String query = "SELECT * FROM items";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (!resultSet.isBeforeFirst()) {
                stockTextArea.setText("Stock is empty.");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Stock:\n");
                while (resultSet.next()) {
                    String itemName = resultSet.getString("name");
                    int quantity = resultSet.getInt("quantity");
                    sb.append(itemName).append(": ").append(quantity).append("\n");
                }
                stockTextArea.setText(sb.toString());
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching stock: " + ex.getMessage());
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
                        // Insert into database
                        String insertQuery = "INSERT INTO items (name, quantity) VALUES (?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                        preparedStatement.setString(1, itemName);
                        preparedStatement.setInt(2, quantity);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();

                        JOptionPane.showMessageDialog(this, itemName + " added to stock with quantity " + quantity);
                    } else {
                        JOptionPane.showMessageDialog(this, "Quantity must be a non-negative number.");
                    }
                } catch (NumberFormatException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error adding item: " + ex.getMessage());
                }
            }
        }
    }

    private void updateQuantity() {
        String itemName = JOptionPane.showInputDialog(this, "Enter item name to update quantity:");
        if (itemName != null && !itemName.trim().isEmpty()) {
            try {
                // Check if item exists
                String checkQuery = "SELECT * FROM items WHERE name=?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, itemName);
                ResultSet resultSet = checkStatement.executeQuery();

                if (!resultSet.isBeforeFirst()) {
                    JOptionPane.showMessageDialog(this, "Item not found in stock.");
                } else {
                    resultSet.next();
                    int currentQuantity = resultSet.getInt("quantity");
                    resultSet.close();

                    String newQuantityStr = JOptionPane.showInputDialog(this, "Enter new quantity:");
                    if (newQuantityStr != null && !newQuantityStr.trim().isEmpty()) {
                        try {
                            int newQuantity = Integer.parseInt(newQuantityStr);
                            if (newQuantity >= 0) {
                                // Update quantity in database
                                String updateQuery = "UPDATE items SET quantity=? WHERE name=?";
                                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                                updateStatement.setInt(1, newQuantity);
                                updateStatement.setString(2, itemName);
                                updateStatement.executeUpdate();
                                updateStatement.close();

                                JOptionPane.showMessageDialog(this, itemName + " quantity updated to " + newQuantity);
                            } else {
                                JOptionPane.showMessageDialog(this, "Quantity must be a non-negative number.");
                            }
                        } catch (NumberFormatException | SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error updating quantity: " + ex.getMessage());
                        }
                    }
                }

                checkStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error checking item: " + ex.getMessage());
            }
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
