package lowstock.lowstockservice.business;

import lowstock.lowstockservice.helper.ProductInfo;
import lowstock.lowstockservice.persistence.Product_CRUD;

import java.util.ArrayList;
import java.util.List;

public class LowStockService {

    // GET ALL PRODUCTS FROM DB
    public static List<ProductInfo> fetchAllProducts() {
        return Product_CRUD.getAllProducts();
    }

    // GET LOW STOCK PRODUCTS
    public static List<ProductInfo> getLowStockProducts() {

        List<ProductInfo> all = fetchAllProducts();
        List<ProductInfo> lowStock = new ArrayList<>();

        for (ProductInfo p : all) {
            if (isLowStock(p.getCurrentStock(), p.getThreshold())) {
                lowStock.add(p);
            }
        }

        return lowStock;
    }

    // GET SINGLE PRODUCT
    public static ProductInfo getProductById(int productId) {
        return Product_CRUD.getProduct(productId);
    }

    // RULE
    public static boolean isLowStock(int currentStock, int threshold) {
        return currentStock <= threshold;
    }
}