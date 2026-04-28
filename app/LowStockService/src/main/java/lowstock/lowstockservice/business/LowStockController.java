package lowstock.lowstockservice.business;

import lowstock.lowstockservice.helper.ProductInfo;
import java.util.List;

public class LowStockController {

    public static List<ProductInfo> getAllLowStock() {
        return LowStockService.getLowStockProducts();
    }

    public static ProductInfo checkSingleProduct(int productId) {
        return LowStockService.getProductById(productId);
    }

    public static boolean isLow(ProductInfo product) {
        if (product == null) return false;

        return LowStockService.isLowStock(
                product.getCurrentStock(),
                product.getThreshold()
        );
    }
}