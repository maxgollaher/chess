# chess

## Database Design

| UserDao               |
|-----------------------|
| PK/FK username:String |
| email:String          |
| password:String       |


| AuthTokenDao        |
|---------------------|
| PK authToken:String |
| FK username:String  |


| GameDao                 |
|-------------------------|
| PK gameID:int           |
| FK whiteUsername:String |
| FK blackUsername:String |
| gameName:String         |
| game:chess.Game         |


