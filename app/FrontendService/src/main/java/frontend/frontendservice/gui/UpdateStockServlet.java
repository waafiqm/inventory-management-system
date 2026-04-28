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

@WebServlet("/UpdateStockServlet")
public class UpdateStockServlet extends HttpServlet {

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
        String action = request.getParameter("action");

        JSONObject json = new JSONObject();
        json.put("quantity", quantity);
        json.put("action", action);
        json.put("username", "admin"); // keep simple
        json.put("password", "admin");

        String url = "http://" + System.getenv("inventoryService") + "/InventoryService/webresources/inventory/products/" + productId;

        String token = (String) session.getAttribute("token");

        String result = APIClient.sendRequest("PUT", url, json.toString(), token);

        request.setAttribute("result", result);
        request.getRequestDispatcher("Confirmation.jsp").forward(request, response);
    }
}
