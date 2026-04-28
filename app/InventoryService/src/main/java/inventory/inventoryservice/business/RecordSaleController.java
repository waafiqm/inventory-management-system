package inventory.inventoryservice.business;

import inventory.inventoryservice.helper.ProductInfo;
import inventory.inventoryservice.persistence.Product_CRUD;
import inventory.inventoryservice.persistence.Sale_CRUD;

public class RecordSaleController {

    public static ProductInfo processSale(
            int productId,
            int quantity
    ) {

        // Retrieve product
        ProductInfo product = Product_CRUD.getProduct(productId);
        if (product == null) {
            return null;
        }

        // Check stock availability
        if (product.getCurrentStock() < quantity) {
            return null;
        }

        // Record sale
        Sale_CRUD.insertSale(productId, 0, quantity);

        // Update stock
        product = InventoryService.updateStock(productId, -quantity);

        // Publish async message so LowStockService stays in sync
        if (product != null) {
            try {
                String msg = "STOCK_UPDATE"
                        + ":" + product.getProductId()
                        + ":" + product.getName()
                        + ":" + product.getCurrentStock()
                        + ":" + product.getThreshold();
                Messaging.sendMessage(msg);
            } catch (Exception e) {
                System.out.printf("Messaging error (RecordSale): %s%n", e.getMessage());
                e.printStackTrace();
            }
        }

        return product;
    }
}