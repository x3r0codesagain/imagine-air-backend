Disclaimer:
This project is a personal project will not and should not be used for commercial purposes

How to run:
1. Open with IntelliJ
2. Find ImagineAirApplication and run it
3. Should data be unavailable please run api /admin/create/bulk
4. /admin apis can only be used by authenticated users as admin
5. To login as admin use Postman
API: http://localhost:8080/api/v1/users/login
Request :
```json
{
  "email": "name20@gmail.com",
  "password": "password"
}
```


6. Api for create data bulk:
http://localhost:8080/api/v1/flights/admin/create/bulk
Params: days: int
Response:
```json

```
