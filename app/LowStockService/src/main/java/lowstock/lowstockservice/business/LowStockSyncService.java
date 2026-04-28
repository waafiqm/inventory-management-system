package lowstock.lowstockservice.business;

import lowstock.lowstockservice.helper.ProductInfo;
import lowstock.lowstockservice.persistence.Product_CRUD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class LowStockSyncService {

    private static String getInventoryUrl() {
        return "http://" + System.getenv("inventoryService") + "/InventoryService/webresources/inventory/products";
    }

    public static void syncAllProducts() {

        try {
            URL url = new URL(getInventoryUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            JSONArray array = new JSONArray(response.toString());

            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);

                ProductInfo p = new ProductInfo();
                p.setProductId(obj.getInt("productId"));
                p.setName(obj.getString("name"));
                p.setCurrentStock(obj.getInt("currentStock"));
                p.setThreshold(obj.getInt("threshold"));

                // ✅ SINGLE SOURCE OF TRUTH
                Product_CRUD.upsertProduct(p);
            }

            System.out.println(">>> FULL LOW STOCK SYNC COMPLETE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}