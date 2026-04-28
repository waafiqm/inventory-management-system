/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.inventoryservice.helper;

import java.time.LocalDateTime;

public class SaleInfo {

    private int saleId;
    private int productId;
    private int userId;
    private int quantitySold;
    private LocalDateTime saleDate;

    public SaleInfo(int saleId, int productId, int userId, int quantitySold, LocalDateTime saleDate) {
        this.saleId = saleId;
        this.productId = productId;
        this.userId = userId;
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
    }

    public int getSaleId() {
        return saleId;
    }

    public int getProductId() {
        return productId;
    }

    public int getUserId() {
        return userId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }
}
