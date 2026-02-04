package inventory;

public class Product {
    private String name;
    private int currentStock;
    private int threshold;

    public Product(String name, int currentStock, int threshold) {
        this.name = name;
        this.currentStock = currentStock;
        this.threshold = threshold;
    }

    public String getName() { return name; }
    public int getCurrentStock() { return currentStock; }
    public void setCurrentStock(int stock) { this.currentStock = stock; }
    public int getThreshold() { return threshold; }
}