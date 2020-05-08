<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Visible Database</title>
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/jquery-3.5.0.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/Sakura.js"></script>
    <style>
        html {
            width: 100%;
            height: 100%;
        }
        body {
            margin: 0;
            padding: 0;
            background: url("images/bg.jpg") no-repeat;
            background-size: cover;
            position: fixed;
        }
        label {
            color: antiquewhite;
        }
        #MainBox {
            margin-left: 350px;
            margin-top: 70px;
        }
        h1 {
            margin-left: 630px;
            margin-top: 100px;
            color: whitesmoke;
        }
        img {
            float: left;
            height: 35px;
            padding-left: 60px;
            width: 160px;
        }
        #hint {
            color: red;
            font-size: medium;
        }
    </style>
    <script !src="">
        function checkA() {
            let check = /^\w{6,15}$/
            if (!check.test($("#username").val()) || !check.test($("#inputPassword3").val())) {
                alert("Please input with right format!    the length of Username and Password is from 6 to 15")
                return false;
            }
            return true;
        }
        $(function () {
            let element = document.getElementById("check");
            element.onclick = function () {
                element.src = "/VisibleDatabase_war_exploded/code?a=" + new Date().getTime();
            }
            $("#username").blur(function () {
                $.get(
                    "userServlet/checkUsername",
                    {
                        username: $("#username").val()
                    },
                    function (data) {
                        if (data.status) {
                            // 用户名已存在
                            $("#UsernameInput").addClass("has-error");
                            $("#hint").html(data.message)
                            $("#UsernameInput").removeClass("has-feedback")
                        } else {
                            // 用户名不存在
                            $("#UsernameInput").addClass("has-feedback")
                            $("#UsernameInput").removeClass("has-error");
                            $("#hint").html("")
                        }
                    }
                )
            })
        })
        $(function () {
            let check = /^\w{6,15}$/
            $("#username,#inputPassword3").blur(function () {
                if (!check.test($(this).val())) {
                    alert("Please input with right format!    the length of Username and Password is from 6 to 15")
                }
            })
        })
        window.onload = function () {
            document.getElementsByTagName("form")[0].onsubmit = function () {
                return checkA();
            }
        }
    </script>
</head>
<body>
    <h1>Register</h1>
    <div class="container" id="MainBox">
        <form class="form-horizontal col-lg-6" action="${pageContext.request.contextPath}/userServlet/register" method="post">
            <div class="form-group" id="UsernameInput">
                <label for="username" class="col-sm-2 control-label">Username</label>
                <div class="col-sm-10">
                    <span id="hint">${sessionScope.message}</span>
                    <input type="text" class="form-control" id="username" placeholder="Username" required name="username" aria-describedby="inputSuccess2Status">
                    <span id="inputSuccess2Status" class="sr-only">(success)</span>
                </div>
            </div>
            <div class="form-group">
                <label for="inputPassword3" class="col-sm-2 control-label">Password</label>
                <div class="col-sm-10">
                    <input type="password" class="form-control" id="inputPassword3" placeholder="Password" required name="password">
                </div>
            </div>
            <div class="form-group">
                <label for="CheckCode" class="col-sm-2 control-label">Verification Code</label>
                <div class="col-sm-6">
                    <input type="text" class="form-control" id="CheckCode" placeholder="Verification Code" required name="code">
                </div>
                <img src="${pageContext.request.contextPath}/code" id="check">
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">Register</button>
                </div>
            </div>
        </form>
    </div>
</body>
</html>