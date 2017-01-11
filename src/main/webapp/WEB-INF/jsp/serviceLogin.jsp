<%--
  Created by IntelliJ IDEA.
  User: DK
  Date: 1/9/17
  Time: 3:06 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <title>Service Login Page</title>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="<c:url value="/resources/theme/js/login.js" />"></script>
    <!-- // Load Javascipt -->

    <!-- Load stylesheets -->
    <link href="<c:url value="/resources/theme/css/style.css" />" rel="stylesheet">
    <script>
        $(document).ready(function(){

            $("#submit_hover").hover(
                    function() {
                        $(this).animate({"opacity": "0"}, "slow");
                    },
                    function() {
                        $(this).animate({"opacity": "1"}, "slow");
                    });
        });
    </script>
</head>
<body>
<div>
    <jsp:include page="header.jsp" flush="true" />
</div>
<div id="wrapper">
    <div id="wrappertop"></div>

    <div id="wrappermiddle">

        <h2>Login</h2>

        <form id="formSubmit">
            <div id="username_input">

                <div id="username_inputleft"></div>

                <div id="username_inputmiddle">
                    <input type="text" autocomplete="off" class="input" name="link" id="appId" placeholder="Your appId">
                    <img id="url_user" src="/resources/theme/images/mailicon.png" alt="">
                </div>

                <div id="username_inputright"></div>

            </div>

            <div id="password_input">

                <div id="password_inputleft"></div>

                <div id="password_inputmiddle">
                    <input type="password" class="input" name="link" id="sig">
                    <img id="url_password" src="/resources/theme/images/passicon.png" alt="">
                </div>

                <div id="password_inputright"></div>

            </div>

            <div id="submitButton">
                <input type="image" src="/resources/theme/images/submit_hover.png" id="submit1" value="Sign In">
                <input type="image" src="/resources/theme/images/submit.png" id="submit2" value="Sign In">
            </div>
        </form>
        <div id="err_area">
            <p id="error"></p>
        </div>
    </div>

    <div id="wrapperbottom"></div>

    <div id="powered">
    </div>
</div>

</body>
<script>
    window.onload = function() {
        $("#submit1").click(function(event){
            event.preventDefault();
            onSubmit();
        });
        $("#submit2").click(function(event){
            event.preventDefault();
            onSubmit();
        });
    }
    function resultLogin(result, data) {
        if (result) {
            window.location.href=data.link;
        } else {
            var err = document.getElementById('error');
            err.textContent='login service fail'
        }
    }
    function onSubmit(){
        var appId = document.getElementById("appId").value;
        var sig = document.getElementById("sig").value;
        loginService(appId, sig, resultLogin);
    }

</script>
</html>
