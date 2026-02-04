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
    
    <p>
        Stock Available: <b><%= p.getCurrentStock() %></b>
        <% if (p.getCurrentStock() <= p.getThreshold()) { %>
            <span style="color:red; font-weight:bold; margin-left:10px;">
                (Low Stock Alert!)
            </span>
        <% } %>
    </p>

    <hr>
    <h3>Record Sale Use Case</h3>
    <form action="InventoryServlet" method="post">
        <input type="hidden" name="action" value="sale">
        Enter Quantity to Sell: 
        <input type="number" name="quantity" min="1" max="<%= p.getCurrentStock() %>" required>
        <input type="submit" value="Submit Sale">
    </form>

    <hr>
    <h3>Manage Inventory (Add/Remove Stock)</h3>
    <form action="InventoryServlet" method="post">
        Amount: <input type="number" name="quantity" min="1" required>
        <input type="submit" name="action" value="add">
        <input type="submit" name="action" value="remove">
    </form>
    
    <br>
    <a href="index.html">Logout</a>
</body>
</html>