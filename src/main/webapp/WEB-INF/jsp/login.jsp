<%--
  Created by IntelliJ IDEA.
  User: DK
  Date: 11/27/16
  Time: 12:40 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/theme/css/main.css" />" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/theme/js/jquery-3.1.1.min.js" />"></script>
    <title>DAA SERVICE PROVIDER LOGIN PAGE</title>
</head>
<body>
<div id="hello" class="hello">DAA SERVICE PROVIDER LOGIN PAGE</div>
<div id="name" class="info">
    <div>Service Name</div>
    <div>${serviceName}</div>
</div>
<div id="certificate" class="info">
    <div>Service Certificate</div>
    <div>${serviceCertificate}</div>
</div>
</body>
<script type="text/javascript" src="<c:url value="/resources/theme/js/hello.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/theme/js/communication.js" />"></script>
</html>
