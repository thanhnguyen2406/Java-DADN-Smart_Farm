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
         "message": "User already existed"     
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

### Devices APIs

#### 1. Add device
- **URL**: `POST /smart-farm/devices/add`
- **Description**: Thêm thiết bị.
- **Request Body**:
  ```json
  {
     "name": "Smart light",
     "type": "SENSOR_TRIGGER",
     "feedsList": {
        "fan": 3023664
     }
  }
  ```
- **Response**:
    - `200 OK`: Added successfully.
      ```json
      {
          "code": 200,
          "message": "Device added successfully",
          "authenticated": true
      }
      ```
    - `409 Feeds existed`
      ```json
      {
         "code": 409,
         "message": "Feed already existed"     
      }
      ```

#### 2. Update device
- **URL**: `PUT /smart-farm/devices/update`
- **Description**: Cập nhật thiết bị.
- **Request Body**:
  ```json
  {
     "id": 1,
     "name": "Ultra light",
     "status": "INACTIVE",
     "feedsList": {
        "fan": 3023664
     }
  }
  ```
- **Response**:
    - `200 OK`: Added successfully.
      ```json
      {
          "code": 200,
          "message": "Device updated successfully",
          "authenticated": true
      }
      ```
    - `409 Feeds existed`
      ```json
      {
         "code": 409,
         "message": "Feed already existed"     
      }
      ```
    - `404 Device not found`
        ```json
          {
            "statusCode": 404,
            "message": "Device not found"     
          }
        ```

#### 3. Delete device
- **URL**: `DELETE /smart-farm/devices/delete/{id}`
- **Description**: Xóa thiết bị.
- **Path variable**: long id
- **Response**:
    - `200 OK`: Deleted successfully.
      ```json
      {
          "code": 200,
          "message": "Device deleted successfully",
          "authenticated": true
      }
      ```
    - `404 Device not found`
        ```json
          {
            "statusCode": 404,
            "message": "Device not found"     
          }
        ```

#### 4. Encode device
- **URL**: `GET /smart-farm/devices/encode/{id}`
- **Description**: Mã hóa thiết bị.
- **Path variable**: long id
- **Response**:
    - `200 OK`: Encoded successfully.
      ```json
      {
          "code": 200,
          "message": "Device encoded successfully",
          "authenticated": true,
          "encodedFeeds": "ZmFuOjMwMjM2NjQ7"
      }
      ```
    - `404 Device not found`
        ```json
          {
            "statusCode": 404,
            "message": "Device not found"     
          }
        ```

#### 5. Assign device
- **URL**: `GET /smart-farm/devices/assign`
- **Description**: Gán thiết bị cho người dùng.
- **Request Param**: encodedFeeds : ZmFuOjMwMjM2NjQ7
- **Response**:
    - `200 OK`: Assigned successfully.
      ```json
      {
          "code": 200,
          "message": "Device has assigned successfully",
          "authenticated": true
      }
      ```
    - `400 Invalid encoded device`
      ```json
      {
         "code": 400,
         "message": "Encoded device string is invalid"     
      }
      ```