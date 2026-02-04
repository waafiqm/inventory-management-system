package inventory;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/InventoryServlet")
public class InventoryServlet extends HttpServlet {
    // Static instance persists the stock while the server runs
    private static Product p = new Product("Smartphone", 100, 5);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("uname") == null) {
            response.sendRedirect("index.html");
            return;
        }

        // Pass the static 'p' which contains the latest stock amount
        request.setAttribute("productInfo", p);
        RequestDispatcher rd = request.getRequestDispatcher("viewInventory.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("uname") == null) {
            response.sendRedirect("index.html");
            return;
        }

        String qtyStr = request.getParameter("quantity");
        if (qtyStr != null && !qtyStr.isEmpty()) {
            int qty = Integer.parseInt(qtyStr);
            
            // Update the static product
            p.setCurrentStock(p.getCurrentStock() - qty);
            
            boolean lowStock = p.getCurrentStock() <= p.getThreshold();

            request.setAttribute("updatedProduct", p);
            request.setAttribute("isLow", lowStock);

            RequestDispatcher rd = request.getRequestDispatcher("confirmation.jsp");
            rd.forward(request, response);
        } else {
            // If no quantity, just go back to the inventory view
            doGet(request, response);
        }
    }
}