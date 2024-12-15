import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

 class BookManagementApp extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable bookTable, authorTable, clientTable, orderTable;
    private DefaultTableModel bookModel, authorModel, clientModel, orderModel;
    private JTextField tfQuery;
    private JButton btnExecute;
    private JLabel imageLabel;

    public BookManagementApp() {
        setTitle("Book-Trip");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Цветовая схема
        Color neonPurple = new Color(138, 43, 226);
        Color darkBackground = Color.BLACK;
        Color textColor = Color.WHITE;

        getContentPane().setBackground(darkBackground);

        // Добавление картинки
        imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon("C://Users//1//Pictures//Roblox/book.jpg"); // Укажите путь к картинке
        Image scaledImage = imageIcon.getImage().getScaledInstance(900, 300, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(darkBackground);

        add(imageLabel, BorderLayout.NORTH);

        // Панель с вкладками
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(darkBackground);
        tabbedPane.setForeground(textColor);

        // Вкладки
        tabbedPane.addTab("Books", createTablePanel("SELECT * FROM book", "Books"));
        tabbedPane.addTab("Authors", createTablePanel("SELECT * FROM author", "Authors"));
        tabbedPane.addTab("Clients", createTablePanel("SELECT * FROM client", "Clients"));
        tabbedPane.addTab("Orders", createTablePanel("SELECT * FROM \"Order\"", "Orders"));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createTablePanel(String query, String type) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Consolas", Font.PLAIN, 14));
        table.setGridColor(new Color(138, 43, 226));
        table.setSelectionBackground(new Color(138, 43, 226));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Выполнение запроса для заполнения таблицы при загрузке вкладки
        executeQueryForTable(query, model);

        return panel;
    }

    private void executeQuery(String query) {
        String url = "jdbc:postgresql://localhost:5432/book_trip"; // Замените на вашу базу данных
        String username = "postgres";                       // Имя пользователя
        String password = "123";                       // Пароль

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DefaultTableModel model = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            JTable selectedTable = getSelectedTable();
            if (selectedTable != null) {
                selectedTable.setModel(model);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executeQueryForTable(String query, DefaultTableModel model) {
        String url = "jdbc:postgresql://localhost:5432/book_trip"; // Замените на вашу базу данных
        String username = "postgres";                       // Имя пользователя
        String password = "123";                       // Пароль

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTable getSelectedTable() {
        int selectedIndex = tabbedPane.getSelectedIndex();

        switch (selectedIndex) {
            case 0: return bookTable;
            case 1: return authorTable;
            case 2: return clientTable;
            case 3: return orderTable;
            default: return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookManagementApp().setVisible(true);
        });
    }
}
