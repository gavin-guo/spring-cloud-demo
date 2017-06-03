<html>
<body>
亲爱的${param.nickName}:<br>
<br>
您在Spring Cloud Demo系统的帐号已创建成功。<br>
登录名：${param.loginName}<br>
<br>
请点击以下链接来激活您的帐号。<br>
<a href="http://localhost:8000/users/activation?user_id=${param.userId}">http://localhost:8000/users/activation?user_id=${param.userId}</a>
<br>
</body>
</html>