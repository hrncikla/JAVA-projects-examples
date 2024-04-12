package lab04;

import java.util.ArrayList;

/**
 * This class represents a receipt with a name, ITIN, total amount and a list of items.
 * The receipt keeps track of the total amount and allows adding or removing items.
 */
public class Receipt {

    private String name;
    private String itin;
    private double total;
    private final ArrayList<Item> items;

    /**
     * Constructs a new receipt with the specified name and ITIN.
     *
     * @param name The name of the receipt.
     * @param itin The ITIN associated with the receipt.
     */
    Receipt(String name, String itin) {
        this.total = 0.0;
        this.items = new ArrayList<>();
        this.name = name;
        this.itin = itin;
    }

    /**
     * Represents an item in the receipt with a name, quantity, and unit price.
     */
    public record Item(String name, int amount, double unitPrice) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItin() {
        return itin;
    }

    public void setItin(String itin) {
        this.itin = itin;
    }

    /**
     * Updates the total of the receipt based on the provided item.
     * If 'add' is true, the item's cost is added to the total amount.
     * If 'add' is false, the item's cost is subtracted from the total amount.
     *
     * @param item The item to calculate the cost from.
     * @param add  A boolean indicating whether to add or subtract the item's cost.
     */
    private void calculateTotal(Item item, boolean add) {
        if (add) {
            total += item.amount() * item.unitPrice();
        } else {
            total -= item.amount() * item.unitPrice();
        }
    }

    public double getTotal() {
        return total;
    }

    public void addNewItem(String name, int amount, int unitPrice) {
        Item newItem = new Item(name, amount, unitPrice);
        items.add(newItem);
        calculateTotal(newItem, true);
    }

    public void removeItem(String itemName) {
        for (Item item : items) {
            if (item.name().equals(itemName)) {
                items.remove(item);
                calculateTotal(item, false);
                return;
            }
        }
        System.out.println("Item not found: " + itemName);
    }

    public void printReceipt() {
        System.out.println("Name: " + name);
        System.out.println("ITIN: " + itin);
        System.out.println("Total: " + total);
        System.out.println("Items: ");

        if (!items.isEmpty()) {
            for (Item item : items) {
                System.out.println(" - " + item.name() + ": amount " + item.amount() + ", unit price " + item.unitPrice());
            }
        } else {
            System.out.println("No items.");
        }
    }
}

