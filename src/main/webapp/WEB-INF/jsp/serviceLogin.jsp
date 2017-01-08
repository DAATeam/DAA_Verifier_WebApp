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
    <script type="text/javascript" src="<c:url value="/resources/theme/js/rainbows.js" />"></script>
    <!-- // Load Javascipt -->

    <!-- Load stylesheets -->
    <link href="<c:url value="/resources/theme/css/style.css" />" rel="stylesheet">
    <script>
        $(document).ready(function(){

            $("#submit1").hover(
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

<div id="wrapper">
    <div id="wrappertop"></div>

    <div id="wrappermiddle">

        <h2>Login</h2>

        <div id="username_input">

            <div id="username_inputleft"></div>

            <div id="username_inputmiddle">
                <form>
                    <input type="text" name="link" id="url" value="Your appId" onclick="this.value = ''">
                    <img id="url_user" src="/resources/theme/images/mailicon.png" alt="">
                </form>
            </div>

            <div id="username_inputright"></div>

        </div>

        <div id="password_input">

            <div id="password_inputleft"></div>

            <div id="password_inputmiddle">
                <form>
                    <input type="password" name="link" id="url" value="Your Personal Signature" onclick="this.value = ''">
                    <img id="url_password" src="/resources/theme/images/passicon.png" alt="">
                </form>
            </div>

            <div id="password_inputright"></div>

        </div>

        <div id="submit">
            <form>
                <input type="image" src="/resources/theme/images/submit_hover.png" id="submit1" value="Sign In">
                <input type="image" src="/resources/theme/images/submit.png" id="submit2" value="Sign In">
            </form>
        </div>


        <div id="links_left">

            <a href="#">Forgot your Password?</a>

        </div>

        <div id="links_right"><a href="#">Not a Member Yet?</a></div>

    </div>

    <div id="wrapperbottom"></div>

    <div id="powered">
    </div>
</div>

</body>
</html>
