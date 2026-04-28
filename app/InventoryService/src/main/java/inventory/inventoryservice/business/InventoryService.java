/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.inventoryservice.business;

import inventory.inventoryservice.helper.ProductInfo;
import inventory.inventoryservice.persistence.Product_CRUD;

public class InventoryService {

    public static ProductInfo updateStock(int productId, int deltaQuantity) {

        ProductInfo product = Product_CRUD.getProduct(productId);

        if (product == null) {
            return null;
        }

        int newStock = product.getCurrentStock() + deltaQuantity;
        product.setCurrentStock(newStock);

        Product_CRUD.updateStock(productId, newStock);

        return product;
    }
}
