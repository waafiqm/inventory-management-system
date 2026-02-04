<%@ page import="inventory.Product" %>
<html>
<head><title>Dashboard</title></head>
<body>
    <% 
        String user = (String) session.getAttribute("uname"); 
        Product p = (Product) request.getAttribute("productInfo"); 
    %>
    
    <h2>Welcome, <%= user %></h2>
    <p>Current Item: <b><%= p.getName() %></b></p>
    <p>Stock Available: <b><%= p.getCurrentStock() %></b></p>

    <hr>
    <h3>Record Sale Use Case</h3>
    <form action="InventoryServlet" method="post">
        Enter Quantity to Sell: <input type="number" name="quantity" required>
        <input type="submit" value="Submit Sale">
    </form>
</body>
</html>