/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.frontendservice.gui;

import frontend.frontendservice.helper.APIClient;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import org.json.JSONObject;

@WebServlet("/RecordSaleServlet")
public class RecordSaleServlet extends HttpServlet {

    private static String getInventoryServiceUrl() {
        return "http://" + System.getenv("inventoryService") + "/InventoryService/webresources/inventory/sales";
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("index.html");
            return;
        }

        int productId = Integer.parseInt(request.getParameter("product_id"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        JSONObject json = new JSONObject();
        json.put("productId", productId);
        json.put("quantity", quantity);

        String token = (String) session.getAttribute("token");

        String result = APIClient.sendRequest("POST", getInventoryServiceUrl(), json.toString(), token);

        request.setAttribute("result", result);
        request.getRequestDispatcher("Confirmation.jsp").forward(request, response);
    }
}
