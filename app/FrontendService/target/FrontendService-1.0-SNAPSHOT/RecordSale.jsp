<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.html");
        return;
    }
%>

<html>
<head>
    <title>Record Sale</title>
</head>
<body style="font-family: sans-serif; padding: 20px;">

<h2>Record Sale</h2>

<form action="RecordSaleServlet" method="post">
    <table cellpadding="8">
        <tr>
            <td>Product ID:</td>
            <td><input type="number" name="product_id" required></td>
        </tr>
        <tr>
            <td>Quantity Sold:</td>
            <td><input type="number" name="quantity" min="1" required></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Submit Sale">
            </td>
        </tr>
    </table>
</form>

<br>
<a href="ViewInventoryServlet">Back to Inventory</a>

</body>
</html>