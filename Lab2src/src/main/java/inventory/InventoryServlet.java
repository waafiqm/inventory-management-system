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
        String action = request.getParameter("action"); // "sale", "add", or "remove"

        if (qtyStr != null && !qtyStr.isEmpty()) {
            try {
                int qty = Integer.parseInt(qtyStr);

                if (action.equals("sale")) {
                    // Logic for a Sale (Goes to confirmation)
                    if (qty > 0 && qty <= p.getCurrentStock()) {
                        p.setCurrentStock(p.getCurrentStock() - qty);
                        boolean lowStock = p.getCurrentStock() <= p.getThreshold();
                        request.setAttribute("updatedProduct", p);
                        request.setAttribute("isLow", lowStock);
                        request.getRequestDispatcher("confirmation.jsp").forward(request, response);
                    } else {
                        doGet(request, response);
                    }
                } 
                else if (action.equals("add")) {
                    // Check for positive integer to prevent "adding" negative numbers
                    if (qty > 0) {
                        p.setCurrentStock(p.getCurrentStock() + qty);
                    }
                    doGet(request, response);
                } 
                else if (action.equals("remove")) {
                    // Check if the amount is positive AND we have enough to remove
                    if (qty > 0 && qty <= p.getCurrentStock()) {
                        p.setCurrentStock(p.getCurrentStock() - qty);
                    }
                    // If invalid, we just refresh via doGet, and no change happens to 'p'
                    doGet(request, response);
                }

            } catch (NumberFormatException e) {
                doGet(request, response);
            }
        } else {
            doGet(request, response);
        }
    }
}