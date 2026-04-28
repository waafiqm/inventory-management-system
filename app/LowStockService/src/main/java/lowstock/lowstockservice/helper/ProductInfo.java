package lowstock.lowstockservice.helper;

public class ProductInfo {

    private int productId;
    private String name;
    private int currentStock;
    private int threshold;

    public ProductInfo() {}

    public ProductInfo(int productId, String name, int currentStock, int threshold) {
        this.productId = productId;
        this.name = name;
        this.currentStock = currentStock;
        this.threshold = threshold;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCurrentStock() { return currentStock; }
    public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }

    public int getThreshold() { return threshold; }
    public void setThreshold(int threshold) { this.threshold = threshold; }
}