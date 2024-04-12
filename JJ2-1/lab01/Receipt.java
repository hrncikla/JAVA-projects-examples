package lab01;

public class Receipt {
    private String name;
    private String itin;
    private int total;
    private Item[] items;

    Receipt(int total) {
        this.total = total;
        this.items = null;
        this.name = "";
        this.itin = "";
    }

    public class Item {

        private final String name;
        private final int amount;
        private final int unitPrice;

        public Item(String name, int amount, int unitPrice) {
            this.name = name;
            this.amount = amount;
            this.unitPrice = unitPrice;
        }

        public String getName() {
            return name;
        }

        public int getAmount() {
            return amount;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Item[] getItems() {
        return items;
    }

    public void addNewItem(String name, int amount, int unitPrice) {
        Item newItem = new Item(name, amount, unitPrice);

        if (items == null) {
            items = new Item[]{newItem};
        } else {
            Item[] newItems = new Item[items.length + 1];
            System.arraycopy(items, 0, newItems, 0, items.length);
            newItems[items.length] = newItem;
            items = newItems;
        }
    }

    public void printReceipt() {
        System.out.println("Name: " + name);
        System.out.println("ITIN: " + itin);
        System.out.println("Total: " + total);
        System.out.println("Items: ");

        if (items != null) {
            for (Item item : items) {
                System.out.println(" - " + item.getName() + ": amount " + item.getAmount() + ", unit price " + item.getUnitPrice());
            }
        } else {
            System.out.println("No items.");
        }
    }
}
