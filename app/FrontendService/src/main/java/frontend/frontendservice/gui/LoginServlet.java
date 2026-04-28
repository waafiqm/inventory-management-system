package frontend.frontendservice.gui;

import frontend.frontendservice.helper.JWTUtil;
import frontend.frontendservice.helper.UserInfo;
import frontend.frontendservice.persistence.User_CRUD;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserInfo user = User_CRUD.authenticate(username, password);

        if (user == null) {
            request.getRequestDispatcher("LoginFailed.jsp").forward(request, response);
        } else {

            String token = JWTUtil.generateToken(username);

            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("token", token);

            response.sendRedirect("ViewInventoryServlet");
        }
    }
}