package lab04;

import java.sql.*;

/**
 * A class representing a Receipt Database client for interacting with a PostgreSQL database.
 * Implements AutoCloseable for resource management.
 */
public class ReceiptDatabase implements AutoCloseable {

    private final PreparedStatement getReceiptByName;

    private final PreparedStatement insertReceipt;
    private final PreparedStatement deleteReceipt;
    private final PreparedStatement updateReceiptName;
    private final PreparedStatement updateReceiptItin;
    private final PreparedStatement updateReceiptTotal;

    private final PreparedStatement insertItemToReceipt;
    private final PreparedStatement deleteItem;
    private final PreparedStatement deleteItemReceiptId;

    /**
     * Initializes the Receipt Database with prepared statements.
     */
    public ReceiptDatabase(Connection con) throws ReceiptDBException {
        try {

            getReceiptByName = con.prepareStatement("SELECT * FROM receipts WHERE name = ?");

            insertReceipt = con.prepareStatement("INSERT INTO receipts (name, itin) VALUES (?, ?)");
            deleteReceipt = con.prepareStatement("DELETE FROM receipts WHERE name = ?");
            updateReceiptName = con.prepareStatement("UPDATE receipts SET name = ? WHERE name = ?");
            updateReceiptItin = con.prepareStatement("UPDATE receipts SET itin = ? WHERE name = ?");
            updateReceiptTotal = con.prepareStatement("UPDATE receipts SET receipt_total = ? WHERE id = ?");

            insertItemToReceipt = con.prepareStatement("INSERT INTO items (receipt_id, name, amount, price) VALUES (?, ?, ?, ?)");
            deleteItem = con.prepareStatement("DELETE FROM items WHERE name = ?");
            deleteItemReceiptId = con.prepareStatement("DELETE FROM items WHERE receipt_id = ?");

        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to initialize prepared statements.", e);
        }
    }

    /**
     * Retrieves a receipt from the database by name.
     */
    public Receipt getReceiptByName(String name) throws ReceiptDBException {
        try {
            getReceiptByName.setString(1, name);
            ResultSet resultSet = getReceiptByName.executeQuery();

            if (resultSet.next()) {
                String itin = resultSet.getString("itin");
                return new Receipt(name, itin);
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new ReceiptDBException("Error while getting receipt by name.", e);
        }
    }

    /**
     * Creates a new receipt in the database.
     */
    public Receipt create(String name, String itin) throws ReceiptDBException {
        try {
            insertReceipt.setString(1, name);
            insertReceipt.setString(2, itin);
            insertReceipt.executeUpdate();
            return new Receipt(name, itin);

        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to create new receipt.", e);
        }
    }

    /**
     * Removes a receipt and its associated items from the database.
     */
    public void remove(Receipt receipt) throws ReceiptDBException {
        try {
            int receiptId = getReceiptId(receipt.getName());

            deleteItemReceiptId.setInt(1, receiptId);
            deleteItemReceiptId.executeUpdate();
            deleteReceipt.setString(1, receipt.getName());
            deleteReceipt.executeUpdate();

        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to delete receipt and its items.", e);
        }
    }

    /**
     * Updates the name of a receipt in the database.
     */
    public void updateReceiptName(Receipt receipt, String newName) throws ReceiptDBException {
        try {
            updateReceiptName.setString(1, newName);
            updateReceiptName.setString(2, receipt.getName());
            updateReceiptName.executeUpdate();
            receipt.setName(newName);
        } catch (SQLException e) {
            throw new ReceiptDBException("Error while updating receipt name.", e);
        }
    }

    /**
     * Updates the ITIN of a receipt in the database.
     */
    public void updateReceiptItin(Receipt receipt, String newItin) throws ReceiptDBException {
        try {
            updateReceiptItin.setString(1, newItin);
            updateReceiptItin.setString(2, receipt.getName());
            updateReceiptItin.executeUpdate();
            receipt.setItin(newItin);
        } catch (SQLException e) {
            throw new ReceiptDBException("Error while updating receipt itin.", e);
        }
    }

    /**
     * Updates the total of a receipt in the database.
     */
    public void updateReceiptTotal(Receipt receipt) throws ReceiptDBException {
        try {
            int receiptId = getReceiptId(receipt.getName());
            double newTotal = receipt.getTotal();

            updateReceiptTotal.setDouble(1, newTotal);
            updateReceiptTotal.setInt(2, receiptId);
            updateReceiptTotal.executeUpdate();
        } catch (SQLException e) {
            throw new ReceiptDBException("Error while updating receipt total.", e);
        }
    }

    /**
     * Adds an item to a receipt in the database.
     *
     * @param receipt  The receipt to which the item will be added.
     * @param itemName The name of the item.
     * @param amount   The amount of the item.
     * @param price    The price of the item.
     * @throws ReceiptDBException if an error occurs during the database operation.
     */
    public void addItemToReceipt(Receipt receipt, String itemName, int amount, double price) throws ReceiptDBException {
        try {
            if (receipt != null) {
                int receiptId = getReceiptId(receipt.getName());

                insertItemToReceipt.setInt(1, receiptId);
                insertItemToReceipt.setString(2, itemName);
                insertItemToReceipt.setInt(3, amount);
                insertItemToReceipt.setDouble(4, price);
                insertItemToReceipt.executeUpdate();

                receipt.addNewItem(itemName, amount, (int) price);
                updateReceiptTotal(receipt);

            } else {
                System.out.println("Receipt not found.");
            }
        } catch (SQLException e) {
            throw new ReceiptDBException("Error while adding item to receipt.", e);
        }
    }

    /**
     * Removes an item from a receipt in the database.
     */
    public void removeItem(Receipt receipt, String itemName) throws ReceiptDBException {
        try {
            deleteItem.setString(1, itemName);
            deleteItem.executeUpdate();
            receipt.removeItem(itemName);
            updateReceiptTotal(receipt);

        } catch (SQLException e) {
            throw new ReceiptDBException("Unable to remove item from receipt.", e);
        }
    }

    /**
     * Retrieves the ID of a receipt by name from the database.
     */
    private int getReceiptId(String receiptName) throws SQLException {
        int receiptId = -1;

        try {
            getReceiptByName.setString(1, receiptName);
            ResultSet resultSet = getReceiptByName.executeQuery();

            if (resultSet.next()) {
                receiptId = resultSet.getInt("id");
            }

        } catch (SQLException e) {
            throw new SQLException("Error while getting receipt ID.", e);
        }
        return receiptId;
    }

    /**
     * Closes all prepared statements associated with the Receipt Database.
     */
    @Override
    public void close() {
        try {
            getReceiptByName.close();
            insertReceipt.close();
            deleteReceipt.close();
            updateReceiptName.close();
            updateReceiptItin.close();
            deleteItem.close();
            updateReceiptTotal.close();
            insertItemToReceipt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}