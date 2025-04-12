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
### Rooms APIs
#### 1. Create new room
- **URL**: `POST /smart-farm/room`
- **Description**: Tạo mới trang trại cho trường hợp người dùng sở hữu nhiều trang trại khác nhau.
- **RequestBody**:
    ```json
    {
        "name": "vuon thanh long 3"
    }
    ```
  - **Response**:
    - `200 OK`: Create successfully
      ```json
      {
          "code": 200,
          "message": "Success",
          "authenticated": true,
          "id": 152,
          "name": "vuon thanh long Tri An"
      }
      ```
    - `409 Room already existed`
    ```json
      {
            "code": 409,
            "message": "Room already existed",
            "authenticated": false
      }
    ```
#### 2. Get rooms
- **URL**: `GET /smart-farm/room`
- **Description**: Lấy ra toàn bộ danh sách các trang trại đang quản lí
- **RequestBody**:
```json
{
  "code": 200,
  "message": "Success",
  "authenticated": true,
  "listRoom": [
    {
      "roomId": 52,
      "roomName": "vuon thanh long Bien Hoa"
    },
    {
      "roomId": 53,
      "roomName": "vuon thanh long Binh Thuan"
    },
    {
      "roomId": 102,
      "roomName": "vuon thanh long 3"
    },
    {
      "roomId": 152,
      "roomName": "vuon thanh long Tri An"
    }
  ]
}
```
#### 3. Update name
- **URL**: `PUT /smart-farm/room/update-name`
- **Description**: Đổi tên trang trại
- **RequestBody**:
```json
{
    "id": "52",
    "name": "vuon thanh long Tinh Doi"
}
```
- **Response**:
```json
{
    "code": 200,
    "message": "Success",
    "authenticated": true,
    "roomId": 52,
    "name": "vuon thanh long Tinh Doi"
}
```

### Schedule APIs
#### 1. Create schedule DAILY
- **URL**: `POST /smart-farm/schedule/create`
- **Description**: Dùng để đặt lịch cho một sự kiện cần diễn ra hằng ngày theo một thời gian cụ thể. Ví dụ: tưới cây hằng ngày,...
- **RequestBody**:
```json
{
  "id_device": 1,
  "status": "ACTIVE",
  "description": "Watering dragon fruit",
  "scheduleType": "DAILY",
  "time_from": "06:00",
  "time_to": "07:00"
}
```
- **Response**:
  - `200`: Create successfully
    ```json
    {
        "code": 200,
        "message": "Success",
        "authenticated": true,
        "id_device": 1,
        "status": "ACTIVE",
        "description": "Watering dragon fruit",
        "scheduleType": "DAILY",
        "time_from": "06:00:00",
        "time_to": "07:00:00"
    }
    ```
  - `4002`: Schedule time overlap
  ```json
    {
        "code": 4002,
        "message": "Schedule time overlap",
        "authenticated": false
    }
    ```
#### 2. Create schedule WEEKLY
- **URL**: `POST smart-farm/schedule/create`
- **Description**: Dùng để đặt lịch cho một sự kiện cần diễn ra hằng tuần dựa vào ngày trong tuần, thời gian cụ thể.
- **RequestBody**:
```json
{
    "id_device": 1,
    "status": "ACTIVE",
    "description": "Fill water into the pump",
    "scheduleType": "WEEKLY",
    "weekDay": "THURSDAY",
    "time_from": "06:00",
    "time_to": "06:30"
}
```
- **Response**
  - `200` : Create successfully
  ```json
    {
        "code": 200,
        "message": "Success",
        "authenticated": true,
        "id_device": 1,
        "status": "ACTIVE",
        "description": "Fill water into the pump",
        "scheduleType": "WEEKLY",
        "weekDay": "THURSDAY",
        "time_from": "06:00:00",
        "time_to": "06:30:00"
    }
    ```
  - `4002` : Schedule time overlap
  ```json
    {
        "code": 4002,
        "message": "Schedule time overlap",
        "authenticated": false
    }
    ```

#### 3. Create schedule ONCE
- **URL**: `POST /smart-farm/schedule/create`
- **Description**: Dùng để đặt các lịch đột xuất, không cố định. Ví dụ đặt lịch tưới nước khoảng từ 6h-7h từ ngày 21/4 - 23/4
- **RequestBody**:
```json
{
  "id_device": 1,
  "status": "INACTIVE",
  "description": "Turn off the fan",
  "scheduleType": "ONCE",
  "startDate": "2025-04-15",
  "endDate": "2025-04-20",
  "time_from": "17:00",
  "time_to": "20:00"
}
```
- **Response**
  - `200`: create successfully
  ```json
    {
        "code": 200,
        "message": "Success",
        "authenticated": true,
        "id_device": 1,
        "status": "INACTIVE",
        "description": "Turn off the fan",
        "scheduleType": "ONCE",
        "startDate": "2025-04-15",
        "endDate": "2025-04-20",
        "time_from": "17:00:00",
        "time_to": "20:00:00"
    }
    ```
  - `4002`: schedule time overlap
  ```json
    {
        "code": 4002,
        "message": "Schedule time overlap",
        "authenticated": false
    }
    ```
#### 4. Get Schedule
- **URL**: `GET /smart-farm/schedule?month={month}&year={year}&id_room={id_room}`
- **Description**: Dùng để lấy ra toàn bộ schedule đã được hẹn trong 1 tháng cụ thể.
- **Example**: `GET /smart-farm/schedule?month=4&year=2025&id_room=52`
- **Response**
```json
{
    "code": 200,
    "message": "Success",
    "authenticated": false,
    "schedules": [
        {
            "id": 604,
            "device": {
                "id": 1,
                "room": {
                    "id": 52,
                    "name": "vuon thanh long Tinh Doi",
                    "email": "hieu@gmail.com"
                },
                "name": "Smart Sensor",
                "userEmail": null,
                "status": "ACTIVE",
                "feedsList": {
                    "humidity": 3023672,
                    "temperature": 3023664
                }
            },
            "status": "INACTIVE",
            "description": "Turn off the fan",
            "scheduleType": "ONCE",
            "startDate": "2025-04-15",
            "endDate": "2025-04-20",
            "time_from": "17:00:00",
            "time_to": "20:00:00"
        },
        {
            "id": 602,
            "device": {
                "id": 1,
                "room": {
                    "id": 52,
                    "name": "vuon thanh long Tinh Doi",
                    "email": "hieu@gmail.com"
                },
                "name": "Smart Sensor",
                "userEmail": null,
                "status": "ACTIVE",
                "feedsList": {
                    "humidity": 3023672,
                    "temperature": 3023664
                }
            },
            "status": "ACTIVE",
            "description": "Watering dragon fruit",
            "scheduleType": "DAILY",
            "time_from": "06:00:00",
            "time_to": "07:00:00"
        },
        {
            "id": 603,
            "device": {
                "id": 1,
                "room": {
                    "id": 52,
                    "name": "vuon thanh long Tinh Doi",
                    "email": "hieu@gmail.com"
                },
                "name": "Smart Sensor",
                "userEmail": null,
                "status": "ACTIVE",
                "feedsList": {
                    "humidity": 3023672,
                    "temperature": 3023664
                }
            },
            "status": "ACTIVE",
            "description": "Fill water into the pump",
            "scheduleType": "WEEKLY",
            "weekDay": "THURSDAY",
            "time_from": "06:00:00",
            "time_to": "06:30:00"
        }
    ]
}
```
#### 5. Delete Schedule
- **URL**: `DELETE /smart-farm/schedule/{scheduleID}`
- **Description**: Dùng để xóa lịch đã đặt dựa vào id của lịch đó.
- **Example**: `DELETE /smart-farm/schedule/503`
- **Response**
  - `200`: delete successfully
    ```json
    {
        "code": 200,
        "message": "Success",
        "authenticated": true
    }
    ```
  - `404`: Schedule not found
    ```json
    {
        "code": 404,
        "message": "Schedule not found",
        "authenticated": false
    }
    ```