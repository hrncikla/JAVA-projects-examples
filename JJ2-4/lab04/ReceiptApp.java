package lab04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ReceiptApp {

    private static final String DB_NAME = "jj2";
    private static final String HOST = "database.inf.upol.cz:5432";
    private static final String USER = "jj2";
    private static final String PASSWORD = "KMIjazykjava_2";

    /**
     * Example of use
     */
    public static void main(String[] args) throws SQLException, ReceiptDBException {
        String connectionURL = "jdbc:postgresql://" + HOST + "/" + DB_NAME;

        try (Connection connection = DriverManager.getConnection(connectionURL, USER, PASSWORD);
             ReceiptDatabase receiptDatabase = new ReceiptDatabase(connection)) {

            Receipt receipt1 = receiptDatabase.create("myPurchase1", "CZ12345678");
            Receipt receipt2 = receiptDatabase.create("myPurchase2", "EN12345678");

            System.out.println("--- Wanted receipt: -------------------------");
            Receipt foundReceipt = receiptDatabase.getReceiptByName("myPurchase1");
            foundReceipt.printReceipt();

            System.out.println("--- Add items: -------------------------");
            receiptDatabase.addItemToReceipt(receipt1, "Apple", 5, 2);
            receiptDatabase.addItemToReceipt(receipt1, "Kiwi", 2, 5);
            receiptDatabase.addItemToReceipt(receipt1, "Banana", 1, 10);
            receipt1.printReceipt();

            receiptDatabase.updateReceiptName(receipt1, "MYPurchase1");
            receiptDatabase.updateReceiptItin(receipt1, "SK12345678");

            System.out.println("--- Remove apple item: -------------------------");
            receiptDatabase.removeItem(receipt1, "Apple");
            receipt1.printReceipt();

            System.out.println("--- Remove databases: -------------------------");
            receiptDatabase.remove(receipt2);
            receiptDatabase.remove(receipt1);
        }
    }
}


