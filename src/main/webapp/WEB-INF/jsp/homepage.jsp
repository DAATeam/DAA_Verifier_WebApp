<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/theme/css/style.css" />" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <title>DAA SERVICE PROVIDER HOMEPAGE</title>
</head>
<body>
<div>
    <jsp:include page="header.jsp" flush="true" />
</div>
<div id="hello" class="hello">DAA SERVICE PROVIDER HOMEPAGE</div>
<div id="time" class="time">${time}</div>
</body>
<script type="text/javascript" src="<c:url value="/resources/theme/js/hello.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/theme/js/communication.js" />"></script>
</html>