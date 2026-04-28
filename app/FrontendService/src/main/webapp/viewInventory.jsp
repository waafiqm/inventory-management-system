<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>

<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.html");
        return;
    }

    String json = (String) request.getAttribute("productsJson");
    
    // DEBUGGING: print raw JSON to the page
    System.out.println("DEBUG: RAW PRODUCTS JSON = " + json); // <-- goes to Tomcat logs
    
    
    JSONArray products = new JSONArray(json);
%>


<!DOCTYPE html>
<html>
<head>
    <title>View Inventory</title>
</head>
<body style="font-family:sans-serif; padding:20px;">

<h2 style="color: green;">Inventory Dashboard</h2>

<table border="1" cellpadding="8">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Stock</th>
        <th>Status</th>
    </tr>

<%
    for (int i = 0; i < products.length(); i++) {
        JSONObject p = products.getJSONObject(i);

        int id = p.getInt("productId");
        String name = p.getString("name");
        int stock = p.getInt("currentStock");
        int threshold = p.getInt("threshold");
%>
    <tr>
        <td><%= id %></td>
        <td><%= name %></td>
        <td><%= stock %></td>
        <td>
            <% if (stock <= threshold) { %>
                <span style="color:red;font-weight:bold;">LOW</span>
            <% } else { %>
                OK
            <% } %>
        </td>
    </tr>
<%
    }
%>
</table>

<hr>

<h3>Actions</h3>

<form action="RecordSale.jsp" method="get" style="display:inline;">
    <input type="submit" value="Record Sale">
</form>

<form action="UpdateStock.jsp" method="get" style="display:inline;">
    <input type="submit" value="Update Stock">
</form>

<form action="LogoutServlet" method="get" style="display:inline;">
    <input type="submit" value="Logout">
</form>

</body>
</html>