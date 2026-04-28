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

@WebServlet("/ViewInventoryServlet")
public class ViewInventoryServlet extends HttpServlet {

    private static String getInventoryServiceUrl() {
        System.out.println("DEBUG inventoryService = " + System.getenv("inventoryService"));
        System.out.println("http://" + System.getenv("inventoryService") + "/InventoryService/webresources/inventory/products");
        return "http://" + System.getenv("inventoryService") + "/InventoryService/webresources/inventory/products";
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("SERVLET HIT");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("index.html");
            return;
        }

        String token = (String) session.getAttribute("token");
        String json = APIClient.sendRequest("GET", getInventoryServiceUrl(), null, token);
        request.setAttribute("productsJson", json);
        request.getRequestDispatcher("viewInventory.jsp").forward(request, response);
    }
}
