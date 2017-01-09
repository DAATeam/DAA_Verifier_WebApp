<%--
  Created by IntelliJ IDEA.
  User: DK
  Date: 11/29/16
  Time: 8:07 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <title>Service Manage Page</title>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <!-- // Load Javascipt -->

    <!-- Load stylesheets -->
    <link href="<c:url value="/resources/theme/css/style.css" />" rel="stylesheet">
</head>
<body>

<div id="wrapper">
    <div id="wrappertop"></div>

    <div id="wrappermiddle">

        <h2 class="serviceName" >Welcome ${serviceName} Service</h2>
        <div class="labelUrl">
            <span>Url For Verify Your Member here</span>
        </div>
        <div class="url">
            <a href=${url}>${url}</a>
        </div>
    </div>

    <div id="wrapperbottom"></div>

    <div id="powered">
    </div>
</div>

</body>
</html>
