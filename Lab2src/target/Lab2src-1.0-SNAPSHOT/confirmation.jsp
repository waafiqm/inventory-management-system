<%@ page import="inventory.Product" %>
<html>
<head><title>Confirmation</title></head>
<body style="font-family: sans-serif; padding: 20px;">
    <h2>Transaction Processed Successfully</h2>
    <% 
        Product p = (Product) request.getAttribute("updatedProduct");
        Boolean low = (Boolean) request.getAttribute("isLow");
    %>
    <p>Product: <b><%= p.getName() %></b></p>
    <p>New Inventory Level: <b><%= p.getCurrentStock() %></b></p>

    <% if (low != null && low) { %>
        <div style="background-color: #ffcccc; border: 2px solid red; padding: 10px; width: 350px; margin-bottom: 20px;">
            <h3 style="color:red; margin:0;">LOW STOCK ALERT!</h3>
            <p>This item has fallen below its threshold of <%= p.getThreshold() %>.</p>
        </div>
    <% } %>

    <hr>
    <form action="InventoryServlet" method="get" style="display: inline;">
        <input type="submit" value="Back to Inventory Dashboard" style="padding: 10px; cursor: pointer;">
    </form>

    <div style="margin-top: 20px;">
        <a href="index.html">Logout</a>
    </div>
</body>
</html>