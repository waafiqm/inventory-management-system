package inventory.inventoryservice.endpoint;

import inventory.inventoryservice.business.RecordSaleController;
import inventory.inventoryservice.business.UpdateStockController;
import inventory.inventoryservice.helper.ProductInfo;
import inventory.inventoryservice.persistence.Product_CRUD;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("inventory")
public class InventoryResource {

    // Get all products
    @GET
    @Path("products")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllProducts() {

        List<ProductInfo> products = Product_CRUD.getAllProducts();
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

    // Record sale
    @POST
    @Path("sales")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String recordSale(String input) {

        JSONObject json = new JSONObject(input);

        int productId = json.getInt("productId");
        int quantity = json.getInt("quantity");

        ProductInfo product = RecordSaleController.processSale(
                productId, quantity
        );

        if (product == null) {
            return new JSONObject().put("error", "Sale failed").toString();
        }

        JSONObject response = new JSONObject();
        response.put("productId", product.getProductId());
        response.put("name", product.getName());
        response.put("currentStock", product.getCurrentStock());

        return response.toString();
    }

    // Update stock
    @PUT
    @Path("products/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateStock(@PathParam("id") int productId, String input) {

        JSONObject json = new JSONObject(input);

        int quantity = json.getInt("quantity");
        String action = json.getString("action");
        String username = json.getString("username");
        String password = json.getString("password");

        ProductInfo product = UpdateStockController.updateStock(
                productId, quantity, action
        );

        if (product == null) {
            return new JSONObject().put("error", "Update failed").toString();
        }

        JSONObject response = new JSONObject();
        response.put("productId", product.getProductId());
        response.put("name", product.getName());
        response.put("currentStock", product.getCurrentStock());

        return response.toString();
    }
}