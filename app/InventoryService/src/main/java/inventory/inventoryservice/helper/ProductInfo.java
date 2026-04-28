/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.inventoryservice.helper;

public class ProductInfo {

    private int productId;
    private String name;
    private int currentStock;
    private int threshold;

    public ProductInfo(int productId, String name, int currentStock, int threshold) {
        this.productId = productId;
        this.name = name;
        this.currentStock = currentStock;
        this.threshold = threshold;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getThreshold() {
        return threshold;
    }
}
