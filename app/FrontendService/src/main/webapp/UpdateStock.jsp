<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.html");
        return;
    }
%>

<html>
<head>
    <title>Update Stock</title>
</head>
<body style="font-family: sans-serif; padding: 20px;">

<h2>Update Product Stock</h2>

<form action="UpdateStockServlet" method="post">
    <table cellpadding="8">
        <tr>
            <td>Product ID:</td>
            <td><input type="number" name="product_id" required></td>
        </tr>
        <tr>
            <td>Quantity:</td>
            <td><input type="number" name="quantity" min="1" required></td>
        </tr>
        <tr>
            <td>Action:</td>
            <td>
                <select name="action">
                    <option value="add">Add Stock</option>
                    <option value="remove">Remove Stock</option>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Update Stock">
            </td>
        </tr>
    </table>
</form>

<br>
<a href="ViewInventoryServlet">Back to Inventory</a>

</body>
</html>