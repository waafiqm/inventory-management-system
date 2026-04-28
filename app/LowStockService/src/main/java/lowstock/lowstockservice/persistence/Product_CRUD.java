package lowstock.lowstockservice.persistence;

import lowstock.lowstockservice.helper.ProductInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Product_CRUD {

    public static List<ProductInfo> getAllProducts() {

        List<ProductInfo> products = new ArrayList<>();
        String sql = "SELECT * FROM Product";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                ProductInfo p = new ProductInfo(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getInt("current_stock"),
                    rs.getInt("threshold")
                );
                products.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

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

    public static void upsertProduct(ProductInfo p) {

        String sql =
            "INSERT INTO Product (product_id, name, current_stock, threshold) " +
            "VALUES (?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "name = VALUES(name), " +
            "current_stock = VALUES(current_stock), " +
            "threshold = VALUES(threshold)";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, p.getProductId());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getCurrentStock());
            ps.setInt(4, p.getThreshold());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
}