## API Specification

### Authentication APIs

#### 1. Login
- **URL**: `POST /smart-farm/auth/login`
- **Description**: Đăng nhập vào hệ thống
- **Request Body**:
  ```json
  {
      "email": "admin",
      "password": "admin"
  }
  ```
- **Response**:
    - `200 OK`: Returns an authentication token.
      ```json
      {
         "code": 200,
         "message": "Login successfully",
         "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJNeUFwcCIsInN1YiI6ImFkbWluIiwiZXhwIjoxNzM0MzY2NzA5LCJpYXQiOjE3MzQzNTk1MDksImp0aSI6IjYwMjVhNzJmLTdmYWQtNGYxYS04Y2FjLTllNjExOTkzMWM0NCIsInNjb3BlIjoiUk9MRV9BRE1JTiJ9.RhGhtazMVhvoFi952G_VnYB-hCd3nbqkEGE7u9wcNK8",
         "authenticated": true
      }
      ```
    - `401 Unauthorized`
      ```json
      {
         "code": 401,
         "message": "Unauthenticated"     
      }
      ```
      - `401 Invalid email`
        ```json
        {
           "code": 401,
           "message": "Please enter email ends with @gmail.com"     
        }
        ```
      - `404 User not found`
        ```json
        {
           "code": 404,
           "message": "User not found"     
        }
        ```
      
### Users APIs

#### 1. Register user
- **URL**: `POST /smart-farm/users/register`
- **Description**: Người dùng tạo tài khoản.
- **Request Body**:
  ```json
  {
     "email": "user1",
     "password": "123",
     "name": "Nguyen Cong Thanh"
  }
  ```
- **Response**:
    - `200 OK`: Register successfully.
      ```json
      {
          "code": 200,
          "message": "Add user successfully",
          "authenticated": false
      }
      ```
    - `401 User existed`
      ```json
      {
         "code": 409,
         "message": "User existed"     
      }
      ```
      - `401 Invalid email`
        ```json
        {
           "code": 401,
           "message": "Please enter email ends with @gmail.com"     
        }
        ```
      - `404 User not found`
        ```json
          {
            "statusCode": 404,
            "message": "User not found"     
          }
        ```
  
