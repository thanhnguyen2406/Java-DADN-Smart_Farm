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

#### 1. Add a device
- **URL**: `POST /smart-farm/devices/add`
- **Description**: Thêm thiết bị.
- **Request Body**:
  ```json
  {
      "name": "Smart Sensor",
      "type": "SENSOR_TRIGGER",
      "feedsList": {
        "fan": {
          "feedId": 3023664,
          "threshold_max": 30.5,
          "threshold_min": 1.0
        },
        "door": {
          "feedId": 3023672,
          "threshold_max": 70.0,
          "threshold_min": 4.0
        }
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

#### 2. Update a device
- **URL**: `PUT /smart-farm/devices/update`
- **Description**: Cập nhật thiết bị.
- **Request Body**:
  ```json
  {
      "id": 3,
      "name": "Smart Light",
      "type": "SENSOR",
      "status": "ACTIVE",
      "feedsList": {
        "light-sensor": {
          "feedId": 3023605,
          "threshold_max": 1200,
          "threshold_min": 100
        }
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

#### 3. Delete a device
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

#### 4. Assign a device to room
- **URL**: `POST /smart-farm/devices/assign-room`
- **Description**: Gán thiết bị vào phòng.
- **Request Body**:
  ```json
  {
    "roomId": 1,
    "deviceId": 22
  }
  ```
- **Response**:
    - `200 OK`: Assigned successfully.
      ```json
      {
          "code": 200,
          "message": "Device assigned to room successfully",
          "authenticated": true
      }
      ```
    - `404 Room not found`
      ```json
      {
         "code": 404,
         "message": "Room not found"     
      }
      ```
    - `404 Device not found`
      ```json
      {
         "code": 404,
         "message": "Device not found"     
      }
      ```

#### 5. Dismiss a device to room
- **URL**: `POST /smart-farm/devices/dismiss-room/{id}`
- **Description**: Gỡ thiết bị khỏi phòng.
- **Path variable**: long id
- **Response**:
    - `200 OK`: Assigned successfully.
      ```json
      {
          "code": 200,
          "message": "Device dismissed from room successfully",
          "authenticated": true
      }
      ```
    - `404 Device not found`
      ```json
      {
         "code": 404,
         "message": "Device not found"     
      }
      ```

#### 6. Get device by Room Id
- **URL**: `GET /smart-farm/devices/room/{id}`
- **Description**: Lấy ra toàn bộ lịch sử theo feed.
- **Path variable**: long id
- **Request Param**:
    - Page: defaultValue = 0
    - Size: defaultValue = 5
- **Response**:
    - `200 OK`: Fetched successfully.
      ```json
      {
          "code": 200,
          "message": "All devices of rooms fetched successfully",
          "authenticated": false,
          "currentPage": 0,
          "totalPages": 1,
          "totalElements": 4,
          "listDeviceDTO": [
              {
                "id": 1,
                "roomId": 1,
                "name": "Smart Sensor 1",
                "type": "SENSOR",
                "status": "INACTIVE",
                "feedsList": {
                    "room1.humid": {
                        "feedId": 3023601,
                        "threshold_max": 30.5,
                        "threshold_min": 1.0
                    },
                    "room1.tempair": {
                        "feedId": 3023602,
                        "threshold_max": 70.0,
                        "threshold_min": 4.0
                    }
                }
            },
            {
                "id": 2,
                "roomId": 1,
                "name": "Smart Sensor 2",
                "type": "CONTROL",
                "status": "INACTIVE",
                "feedsList": {
                    "room1.fan": {
                        "feedId": 3023664,
                        "threshold_max": 40.0,
                        "threshold_min": 15.0
                    }
                }
            }
         ]
      }
      ```
    - `200 OK` No devices
      ```json
      {
         "code": 200,
         "message": "No devices found"     
      }
      ```
    - `404 Room not found`
      ```json
      {
         "code": 404,
         "message": "Room not found"     
      }
      ```

#### 7. Get All Unassigned Devices
- **URL**: `GET /smart-farm/devices/unassign`
- **Description**: Lấy ra toàn bộ lịch sử theo feed.
- **Request Param**:
    - Page: defaultValue = 0
    - Size: defaultValue = 5
- **Response**:
  - `200 OK`: Fetched successfully.
    ```json
    {
        "code": 200,
        "message": "All unassigned devices of rooms fetched successfully",
        "authenticated": false,
        "currentPage": 0,
        "totalPages": 1,
        "totalElements": 4,
        "listDeviceDTO": [
            {
              "id": 21,
              "name": "Smart Sensor 12",
              "type": "SENSOR",
              "status": "INACTIVE",
              "feedsList": {
                  "smoke": {
                      "feedId": 3023622,
                      "threshold_max": 300.0,
                      "threshold_min": 50.0
                  }
              }
          },
          {
              "id": 22,
              "name": "Smart Controller 10",
              "type": "CONTROL",
              "status": "INACTIVE",
              "feedsList": {
                  "fancontrol": {
                      "feedId": 3023623,
                      "threshold_max": 100.0,
                      "threshold_min": 40.0
                  }
              }
          },
          {
              "id": 23,
              "name": "Smart Sensor 13",
              "type": "SENSOR",
              "status": "INACTIVE",
              "feedsList": {
                  "temperature": {
                      "feedId": 3023624,
                      "threshold_max": 40.0,
                      "threshold_min": 5.0
                  }
              }
          }
       ]
    }
    ```
  - `200 OK` No devices
    ```json
    {
       "code": 200,
       "message": "No devices found"     
    }
    ```
  - `404 Room not found`
    ```json
    {
       "code": 404,
       "message": "Room not found"     
    }
    ```

### Rooms APIs
#### 1. Create new room
- **URL**: `POST /smart-farm/rooms/add`
- **Description**: Tạo mới trang trại cho trường hợp người dùng sở hữu nhiều trang trại khác nhau.
- **RequestBody**:
    ```json
    {
        "name": "vuon thanh long 3",
        "roomKey": "room1"
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
#### 2. Get rooms by username
- **URL**: `GET /smart-farm/rooms`
- **Description**: Lấy ra toàn bộ danh sách các trang trại đang quản lí của người đang đăng nhập
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
- **URL**: `PUT /smart-farm/rooms/update`
- **Description**: Đổi tên trang trại
- **RequestBody**:
```json
{
    "id": "52",
    "name": "vuon thanh long Tinh Doi",
    "roomKey": "room52"
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
#### 4. Encode room
- **URL**: `GET /smart-farm/rooms/encode/{id}`
- **Description**: Mã hóa phòng.
- **Path variable**: long id
- **Response**:
    - `200 OK`: Encoded successfully.
      ```json
      {
      "code": 200,
      "message": "Room encoded successfully",
      "authenticated": false,
      "encodedRoom": "MTp2dW9uIHRoYW5oIGxvbmcgVHJpIEFuOlNtYXJ0RmFybVNlY3JldA=="
      }
      ```
    - `404 Room not found`
        ```json
          {
            "statusCode": 404,
            "message": "Room not found"     
          }
        ```

#### 5. Assign room
- **URL**: `GET /smart-farm/rooms/assign`
- **Description**: Gán phòng cho người dùng.
- **Request Param**: 
    - encodedFeeds : MTp2dW9uIHRoYW5oIGxvbmcgVHJpIEFuOlNtYXJ0RmFybVNlY3JldA==
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


### Device Triggers APIs

#### 1. Add a trigger
- **URL**: `POST /smart-farm/triggers/add`
- **Description**: Thêm trigger thiết bị.
- **Request Body**:
  ```json
  {
    "condition": "MAX",
    "sensorFeedKey": "light-sensor",
    "controlFeedKey": "fan",
    "valueSend": "1"
  }
  ```
- **Response**:
    - `200 OK`: Added successfully.
      ```json
      {
          "code": 200,
          "message": "New trigger added successfully",
          "authenticated": true
      }
      ```
    - `409 Trigger existed`
      ```json
      {
         "code": 409,
         "message": "Trigger already existed"     
      }
      ```

#### 2. Update a trigger
- **URL**: `PUT /smart-farm/triggers/update`
- **Description**: Cập nhật trigger thiết bị.
- **Request Body**:
  ```json
  {
    "id": 1,
    "status": "ACTIVE",
    "condition": "MAX",
    "sensorFeedKey": "light-sensor",
    "controlFeedKey": "fan",
    "valueSend": "1"
  }
  ```
- **Response**:
    - `200 OK`: Updated successfully.
      ```json
      {
          "code": 200,
          "message": "Trigger updated successfully",
          "authenticated": true
      }
      ```
    - `404 Trigger not found`
      ```json
      {
         "code": 404,
         "message": "Trigger not found"     
      }
      ```

#### 3. Delete a device
- **URL**: `DELETE /smart-farm/triggers/delete/{id}`
- **Description**: Xóa trigger thiết bị.
- **Path variable**: long id
- **Response**:
    - `200 OK`: Deleted successfully.
      ```json
      {
          "code": 200,
          "message": "Trigger deleted successfully",
          "authenticated": true
      }
      ```
    - `404 Trigger not found`
        ```json
          {
            "statusCode": 404,
            "message": "Trigger not found"     
          }
        ```
#### 4. Get trigger by Device Id
- **URL**: `GET /smart-farm/triggers/device/{id}`
- **Description**: Lấy ra toàn bộ trigger bằng device id.
- **Path variable**: long id
- **Request Param**:
    - Page: defaultValue = 0
    - Size: defaultValue = 5
- **Response**:
    - `200 OK`: Fetched successfully.
      ```json
      {
          "code": 200,
          "message": "Trigger of the device fetched successfully",
          "authenticated": false,
          "currentPage": 0,
          "totalPages": 1,
          "totalElements": 1,
          "listDeviceTriggersDTO": [
            {
                "id": 1,
                "status": "ACTIVE",
                "valueSend": "1",
                "condition": "MAX",
                "sensorFeedKey": "light-sensor",
                "controlFeedKey": "fan"
            }
          ]
      }
      ```
    - `200 OK` No triggers
      ```json
      {
         "code": 200,
         "message": "No triggers found for this device"     
      }
      ```
    - `404 Device not found`
      ```json
      {
         "code": 404,
         "message": "Device not found"     
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
  "feedId": 3023664,
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
        "feedId": 3023664,
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
    "feedId": 3023664,
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
        "feedId": 3023664,
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
  "feedId": 3023664,
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
        "feedId": 3023664,
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
    
### Statistic APIs
- **URL**: `GET /smart-farm/statistic?feedId={feedId}&date={date}`
- **Description**: Dùng để lấy dữ liệu thô và thống kê của 1 ngày cụ thể mà user muốn kiểm tra, cứ cách 10' dữ liệu sẽ lưu vào db 1 lần, 1 ngày có 24 tiếng -> 1 ngày trả về ~144 điểm dữ liệu.
- **Example**: `GET /smart-farm/statistic?feedId=3023665&date=2025-04-16`
- **Response**:
  - `200`: Success
  ```json
    {
    "code": 200,
    "message": "Statistic for 2025-04-16 fetched successfully",
    "authenticated": true,
    "data": [
        {
        "value": 0.0,
        "timeStamp": "2025-04-16T22:15:53"
        },
        {
        "value": 0.0,
        "timeStamp": "2025-04-16T22:25:53"
        },
        {
        "value": 0.0,
        "timeStamp": "2025-04-16T22:35:53"
        },
        {
        "value": 0.0,
        "timeStamp": "2025-04-16T22:45:53"
        },
        {
        "value": 0.0,
        "timeStamp": "2025-04-16T22:55:53"
        },
        {
        "value": 0.0,
        "timeStamp": "2025-04-16T23:05:53"
        },
        {
        "value": 0.0,
        "timeStamp": "2025-04-16T23:15:53"
        },
        {
        "value": 0.0,
        "timeStamp": "2025-04-16T23:25:53"
        },
        {
        "value": 0.0,
        "timeStamp": "2025-04-16T23:35:53"
        }
    ],
    "max": 0.0,
    "min": 0.0,
    "average": 0.0,
    "threshold": false
    }
    ```

### Logs APIs

#### 1. Delete a log
- **URL**: `DELETE /smart-farm/logs/delete/{id}`
- **Description**: Xóa lịch sử.
- **Path variable**: long id
- **Response**:
    - `200 OK`: Deleted successfully.
      ```json
      {
          "code": 200,
          "message": "Log deleted successfully",
          "authenticated": true
      }
      ```
  - `404 Device not found`
      ```json
        {
          "statusCode": 404,
          "message": "Log not found"     
        }
      ```

#### 2. Get logs by feed key
- **URL**: `GET /smart-farm/logs/feed/{feedKey}`
- **Description**: Lấy ra toàn bộ lịch sử theo feed.
- **Path variable**: String feedKey
- **Request Param**:
    - Page: defaultValue = 0
    - Size: defaultValue = 5
- **Response**:
    - `200 OK`: Fetched successfully.
      ```json
      {
          "code": 200,
          "message": "All logs fetched successfully",
          "authenticated": false,
          "currentPage": 0,
          "totalPages": 1,
          "totalElements": 4,
          "listLogDTO": [
            {
              "id": 1,
              "logType": "TRIGGER_MAX",
              "feedKey": "light-sensor",
              "value": "1200.0",
              "createdAt": "2025-04-20 03:15"
            },
            {
              "id": 2,
              "logType": "TRIGGER_MAX",
              "feedKey": "light-sensor",
              "value": "1200.0",
              "createdAt": "2025-04-20 03:15"
            },
            {
              "id": 3,
              "logType": "DATA",
              "feedKey": "light-sensor",
              "value": "533.3333333333334",
              "createdAt": "2025-04-20 03:17"
            },
            {
              "id": 4,
              "logType": "DATA",
              "feedKey": "light-sensor",
              "value": "900.0",
              "createdAt": "2025-04-20 03:18"
            }
          ]
      }
      ```
  - `200 OK` No logs
    ```json
    {
       "code": 200,
       "message": "No logs are found"     
    }
    ```
  - `404 Feed not found`
      ```json
        {
          "statusCode": 404,
          "message": "Feed sensor not found"     
        }
      ```

#### 3. Get logs by feed key and log type
- **URL**: `GET /smart-farm/logs/type`
- **Description**: Lấy ra toàn bộ lịch sử theo feed và log type.
- **Request Param**:
    - Page: defaultValue = 0
    - Size: defaultValue = 5
- **RequestBody**: logType: DATA / TRIGGER_MAX / TRIGGER_MIN
```json
{
  "logType": "DATA",
  "feedKey": "light-sensor"
}
```
- **Response**:
    - `200 OK`: Fetched successfully.
      ```json
      {
          "code": 200,
          "message": "All logs fetched successfully",
          "authenticated": false,
          "currentPage": 0,
          "totalPages": 1,
          "totalElements": 4,
          "listLogDTO": [
                {
                    "id": 3,
                    "logType": "DATA",
                    "feedKey": "light-sensor",
                    "value": "533.3333333333334",
                    "createdAt": "2025-04-20 03:17"
                },
                {
                    "id": 4,
                    "logType": "DATA",
                    "feedKey": "light-sensor",
                    "value": "900.0",
                    "createdAt": "2025-04-20 03:18"
                }
          ]
      }
      ```
  - `200 OK` No logs
      ```json
      {
         "code": 200,
         "message": "No logs are found"     
      }
      ```
  - `404 Feed not found`
      ```json
        {
          "statusCode": 404,
          "message": "Feed sensor not found"     
        }
      ```