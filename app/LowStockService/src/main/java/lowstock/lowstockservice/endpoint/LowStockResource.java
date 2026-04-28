package lowstock.lowstockservice.endpoint;

import lowstock.lowstockservice.business.LowStockController;
import lowstock.lowstockservice.business.LowStockSyncService;
import lowstock.lowstockservice.helper.ProductInfo;
import lowstock.lowstockservice.persistence.Product_CRUD;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("lowstock")
public class LowStockResource {

    // ==============================
    // GET ALL LOW STOCK PRODUCTS
    // ==============================
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getLowStockProducts() {

        List<ProductInfo> products = LowStockController.getAllLowStock();
        JSONArray array = new JSONArray();

        for (ProductInfo p : products) {
            JSONObject obj = new JSONObject();
            obj.put("productId", p.getProductId());
            obj.put("name", p.getName());
            obj.put("currentStock", p.getCurrentStock());
            obj.put("threshold", p.getThreshold());
            array.put(obj);
        }

        return array.toString();
    }

    // ==============================
    // GET SINGLE PRODUCT STATUS
    // ==============================
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSingleProduct(@PathParam("id") int productId) {

        ProductInfo product = LowStockController.checkSingleProduct(productId);

        if (product == null) {
            return new JSONObject()
                    .put("error", "Product not found")
                    .toString();
        }

        JSONObject obj = new JSONObject();
        obj.put("productId", product.getProductId());
        obj.put("name", product.getName());
        obj.put("currentStock", product.getCurrentStock());
        obj.put("threshold", product.getThreshold());
        obj.put("isLowStock",
                LowStockController.isLow(product)
        );

        return obj.toString();
    }

    // ==============================
    // NEW: SYNC ENDPOINT (FROM INVENTORY)
    // ==============================
    @POST
    @Path("/sync")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String syncProduct(String json) {

        try {
            JSONObject obj = new JSONObject(json);

            ProductInfo product = new ProductInfo();
            product.setProductId(obj.getInt("productId"));
            product.setName(obj.getString("name"));
            product.setCurrentStock(obj.getInt("currentStock"));
            product.setThreshold(obj.getInt("threshold"));

            Product_CRUD.upsertProduct(product);

            return new JSONObject()
                    .put("status", "synced")
                    .put("productId", product.getProductId())
                    .toString();

        } catch (Exception e) {
            e.printStackTrace();

            return new JSONObject()
                    .put("status", "error")
                    .put("message", "Sync failed")
                    .toString();
        }
    }
    
    @POST
    @Path("/syncAll")
    @Produces(MediaType.APPLICATION_JSON)
    public String syncAll() {

        LowStockSyncService.syncAllProducts();

        return new JSONObject()
                .put("status", "full sync completed")
                .toString();
    }                                                   
}