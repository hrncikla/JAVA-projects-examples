package lab04;

import java.sql.*;

/**
 * A class representing a client for interacting with a PostgreSQL database.
 * Connects to the database using JDBC, retrieves and prints information from the "receipts" and "items" tables.
 */
public class DatabaseClient {

    private static final String DB_NAME = "jj2";
    private static final String HOST = "database.inf.upol.cz:5432";
    private static final String USER = "jj2";
    private static final String PASSWORD = "KMIjazykjava_2";
    private Connection con;

    /**
     * Connects to the PostgreSQL database using the specified parameters.
     */
    public void connect() throws SQLException {
        String connectionURL = "jdbc:postgresql://" + HOST + "/" + DB_NAME;

        con = DriverManager.getConnection(connectionURL, USER, PASSWORD);
        System.out.println("Connecting to: " + connectionURL);
    }

    /**
     * Closes the database connection.
     */
    public void close() throws SQLException {
        con.close();
    }

    /**
     * Retrieves and prints information from the receipts table.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void printReceipts() throws SQLException {
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM receipts")) {
            ResultSet resultSet = statement.executeQuery();
            System.out.println("Receipts Table:");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String itin = resultSet.getString("itin");
                double total = resultSet.getDouble("receipt_total");

                System.out.println("ID: " + id + ", Name: " + name + ", ITIN: " + itin + ", Total: " + total);
            }
        }
    }

    /**
     * Retrieves and prints information from the items table.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void printItems() throws SQLException {
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM items")) {
            ResultSet resultSet = statement.executeQuery();
            System.out.println("Items Table:");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int receiptId = resultSet.getInt("receipt_id");
                String name = resultSet.getString("name");
                int amount = resultSet.getInt("amount");
                double price = resultSet.getDouble("price");

                System.out.println("ID: " + id + ", Receipt ID: " + receiptId + ", Name: " + name + ", Amount: " + amount + ", Price: " + price);
            }
        }
    }

    public static void main(String[] args) {
        try {
            DatabaseClient client = new DatabaseClient();
            client.connect();
            client.printReceipts();
            client.printItems();
            client.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}