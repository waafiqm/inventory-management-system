package inventory.inventoryservice.persistence;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

import inventory.inventoryservice.helper.ProductInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Product_CRUD {

    private static final String SECRET = "my-secret-key"; // ✅ added

    public static ProductInfo getProduct(int productId) {

        ProductInfo product = null;
        String sql = "SELECT * FROM Product WHERE product_id = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product = new ProductInfo(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getInt("current_stock"),
                    rs.getInt("threshold")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    public static List<ProductInfo> getAllProducts() {

        List<ProductInfo> products = new ArrayList<>();
        String sql = "SELECT * FROM Product";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                products.add(new ProductInfo(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getInt("current_stock"),
                    rs.getInt("threshold")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public static void updateStock(int productId, int newStock) {

        String sql = "UPDATE Product SET current_stock = ? WHERE product_id = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ==========================
        // SYNC TO LOW STOCK SERVICE
        // ==========================
        try {
            ProductInfo product = getProduct(productId);

            if (product == null) return;

            product.setCurrentStock(newStock);

            System.out.println(">>> SENDING SYNC TO LOWSTOCK");

            URL url = new URL(
                "http://" + System.getenv("lowStockService") + "/LowStockService/webresources/lowstock/sync"
            );

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            // 🔥 ADD JWT TOKEN HERE
            String token = JWT.create()
                    .withSubject("inventory-service")
                    .withIssuer("InventoryService")
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                    .sign(Algorithm.HMAC256(SECRET));

            conn.setRequestProperty("Authorization", "Bearer " + token);

            conn.setDoOutput(true);

            String json =
                "{" +
                "\"productId\":" + product.getProductId() + "," +
                "\"name\":\"" + product.getName() + "\"," +
                "\"currentStock\":" + newStock + "," +
                "\"threshold\":" + product.getThreshold() +
                "}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println(">>> SYNC RESPONSE: " + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}