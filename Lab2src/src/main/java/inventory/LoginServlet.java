package inventory;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");

        if (username != null && !username.isEmpty()) {
            // Just set the session
            request.getSession().setAttribute("uname", username);

            // Redirect to InventoryServlet - DO NOT create a new Product(20) here
            response.sendRedirect("InventoryServlet");
        } else {
            response.sendRedirect("index.html");
        }
    }
}