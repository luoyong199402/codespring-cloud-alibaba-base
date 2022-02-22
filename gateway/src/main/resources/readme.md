### spring cloud 身份授权
#### 密码模式
```curl
curl -X POST \
  http://localhost:19999/oauth/token \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -H 'postman-token: 71016f65-4a48-f6cb-f195-708a05bb4902' \
  -d 'client_id=account&client_secret=account123&username=test&password=123456&grant_type=password'
```