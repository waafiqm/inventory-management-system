package inventory.inventoryservice.business;

import inventory.inventoryservice.helper.ProductInfo;
import inventory.inventoryservice.persistence.Product_CRUD;

public class UpdateStockController {

    public static ProductInfo updateStock(
            int productId,
            int quantity,
            String action
    ) {

        // Retrieve product
        ProductInfo product = Product_CRUD.getProduct(productId);
        if (product == null) {
            return null;
        }

        // Determine stock change
        int delta = action.equalsIgnoreCase("add") ? quantity : -quantity;

        // Prevent negative stock
        if (delta < 0 && product.getCurrentStock() < quantity) {
            return null;
        }

        // Update stock
        product = InventoryService.updateStock(productId, delta);

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
                System.out.printf("Messaging error (UpdateStock): %s%n", e.getMessage());
                e.printStackTrace();
            }
        }

        return product;
    }
}