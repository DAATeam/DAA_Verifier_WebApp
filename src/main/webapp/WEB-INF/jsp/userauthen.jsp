<%--
  Created by IntelliJ IDEA.
  User: DK
  Date: 12/22/16
  Time: 6:19 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/theme/css/style.css" />" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.js"></script>
    <title>DAA ${serviceName} SERVICE PROVIDER LOGIN PAGE</title>
</head>
<body>
<div id="wrapper">
    <div id="wrappertop"></div>

    <div id="wrappermiddle">
        <div id="hello" class="hello">DAA ${serviceName} SERVICE PROVIDER LOGIN PAGE</div>
        <p>Welcome You to our service</p>
        <div id="name" class="info">
            <div>Service Name</div>
            <div>${serviceName}</div>
        </div>
        <div id="certificate" class="info">
            <div>Service Status</div>
            <div>${status}</div>
        </div>
        <div id="serviceId" value=${serviceId}></div>
        <div id="authentication" class="authentication">
            <button id="button-authen" value=${enable} onclick="clickAuthen()" disabled>Authentication</button>
        </div>
    </div>

    <div id="wrapperbottom"></div>

    <div id="powered">
    </div>
</div>
</body>
<script type="text/javascript" src="<c:url value="/resources/theme/js/hello.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/theme/js/communication.js" />"></script>
</html>
