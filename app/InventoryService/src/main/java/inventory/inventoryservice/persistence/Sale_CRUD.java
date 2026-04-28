/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.inventoryservice.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Sale_CRUD {

    public static void insertSale(int productId, int userId, int quantitySold) {

        String sql =
            "INSERT INTO Sale (product_id, user_id, quantity_sold) " +
            "VALUES (?, ?, ?)";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            ps.setInt(3, quantitySold);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
