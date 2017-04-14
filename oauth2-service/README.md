## 获取access token

在浏览器地址栏中输入
http://localhost:9999/uaa/oauth/authorize?client_id=acme&response_type=code&redirect_uri=http://www.baidu.com

页面会跳转到一个简易的登录画面，输入正确的用户名和密码，并且approve后，跳转到www.baidu.com，截取地址栏中https://www.baidu.com/?code=9SfnjS 中显示的code。

POST http://acme:secret@localhost:9999/uaa/oauth/token
- header
```
content-type:application/x-www-form-urlencoded
```

- body
```
grant_type : authorization_code
code : ${code}
redirect_uri : http://www.baidu.com
```