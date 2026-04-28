<%@ page import="org.json.JSONObject" %>

<html>
<head>
    <title>Confirmation</title>
</head>
<body style="font-family: sans-serif; padding: 20px;">

<h2>Transaction Successful</h2>

<%
    String result = (String) request.getAttribute("result");

    JSONObject obj = new JSONObject(result);

    int id = obj.getInt("productId");
    String name = obj.getString("name");
    int stock = obj.getInt("currentStock");
%>

<p>Product ID: <b><%= id %></b></p>
<p>Product Name: <b><%= name %></b></p>
<p>Current Stock: <b><%= stock %></b></p>

<hr>

<form action="ViewInventoryServlet" method="get">
    <input type="submit" value="Return to Inventory">
</form>

</body>
</html>