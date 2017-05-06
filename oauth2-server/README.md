#获取access token

## grant_type=authorization_code

在浏览器地址栏中输入
http://localhost:9999/uaa/oauth/authorize?client_id=acme&response_type=code&redirect_uri=http://www.baidu.com

页面会跳转到一个简易的登录画面，输入正确的用户名和密码，并且approve，验证成功后自动跳转到www.baidu.com。
![](https://raw.githubusercontent.com/gavin-guo/spring-cloud-demo/master/oauth2-server/example-images/auth-1.png)
![](https://raw.githubusercontent.com/gavin-guo/spring-cloud-demo/master/oauth2-server/example-images/auth-2.png)

地址栏显示的url中显示了授权code。
![](https://raw.githubusercontent.com/gavin-guo/spring-cloud-demo/master/oauth2-server/example-images/auth-3.png)

以此授权code去换取access token。  
POST http://acme:secret@localhost:9999/uaa/oauth/token
- header
```
Content-Type:application/x-www-form-urlencoded
```

- body
```
grant_type : authorization_code
code : ${code}
redirect_uri : http://www.baidu.com
```

## grant_type=password

POST http://acme:secret@localhost:9999/uaa/oauth/token
- header
```
Content-Type:application/x-www-form-urlencoded
```

- body
```
grant_type : password
username : ${username}
password : ${password}
```

## grant_type=client_credentials

POST http://acme:secret@localhost:9999/uaa/oauth/token
- header
```
Content-Type:application/x-www-form-urlencoded
```

- body
```
grant_type : client_credentials
```

## grant_type=refresh_token

POST http://acme:secret@localhost:9999/uaa/oauth/token
- header
```
Content-Type:application/x-www-form-urlencoded
```

- body
```
grant_type : refresh_token
refresh_token : ${refresh_token}
```