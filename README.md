# Music API

This api provides RESTful endpoints for CRUD operations on Albums Database.

Following are the APIs that are available:
#### Create Album
**Request:**
```
POST http://localhost:8080/albums/create
Body:
Content Type: JSON

{
  "name": "Good Bye & Good Riddance",
  "artist": "Juice WRLD",
  "genre": "Pop",
  "year": 2018
}

```

**Response:**
```
HTTP STATUS: 200
{
    "id": 104,
    "name": "Good Bye & Good Riddance",
    "artist": "Juice WRLD",
    "genre": "Pop",
    "year": 2018
}
```

#### Retrieve Album
**Request:**
```
GET http://localhost:8080/albums/find/103
```
**Response:**
```
HTTP STATUS: 200
{
    "id": 103,
    "name": "Sgt Pepper's Lonely Hearts Club Band",
    "artist": "The Beatles",
    "genre": "Rock",
    "year": 1967
}
```
#### Update Album
**Request:**
```
PUT http://localhost:8080/albums/update/104

BODY:
Content Type: JSON

{
   "name": "Good Bye & Good Riddance",
   "artist": "Juice WRLD",
   "genre": "Pop/Rock",
   "year": 2018
}

```
**Response:**
```
HTTP STATUS: 200
{
    "id": 104,
    "name": "Good Bye & Good Riddance",
    "artist": "Juice WRLD",
    "genre": "Pop/Rock",
    "year": 2018
}
```
#### Delete Album
**Request:**
```
DELETE http://localhost:8080/albums/delete/104
```
**Response:**
```
HTTP STATUS: 200
{"status":"Album Deleted"}
```
#### List of all Artists
**Request:**
```
GET http://localhost:8080/albums/allartist
```
**Response:**

```
HTTP STATUS: 200
[
    "Aerosmith",
    "Air",
    "American Authors",
    "Aretha Franklin",
    "Bad Suns",
    "Bastille",
    "Beck",
    "Beirut",
    "Bj苔rk",
    "Blur",
    .
    .
    .
 ]
```
#### Albums produced by an Artist
**Request:**
```
GET http://localhost:8080/albums/findbyartist/Bob%20Marley
```
**Response:**
```
HTTP STATUS: 200
[
    {
        "id": 63,
        "name": "Exodus",
        "artist": "Bob Marley",
        "genre": "Reggae",
        "year": 1977
    },
    {
        "id": 97,
        "name": "Live!",
        "artist": "Bob Marley",
        "genre": "Reggae",
        "year": 1975
    }
]
```
#### Genre ranking By albums
**Request:**
```
GET http://localhost:8080/albums/stats/1
```
**Response:**
```
HTTP STATUS: 200
{
    "Alternative": 24,
    "Rock": 20,
    "Indie": 11,
    "Pop": 9,
    "Folk": 5,
    "Rap": 4,
    "Jazz": 4,
    "Dance": 4,
    "Reggae": 4,
    .
    .
    .
}
```
#### Year ranking By album Count
**Request:**
```
GET http://localhost:8080/albums/stats/0
```
**Response:**
```
HTTP STATUS: 200
{   
    "2016":9,
    "1994":6,
    "2010":4,
    "1998":4,
    "1977":4,
    .
    .
    .
}
```

## Details of Backend
- Database is in-memory H2 database
- The application is built using spring boot version 2.1.0
- The project is a gradle(4.8.1) based archive project
- Application needs Java 1.8

## How to run the project?
### Option 1
- Make sure you have the Java version on your workstation is atleast 1.8
- Clone the project from [github](https://github.com/rbidanta/musicapi)
- Run the following command to build the project
```
./gradlew clean build
```
- The above command will create a jar file under /build/libs/musicapi-0.1.0.jar
- Use the following command to run the application
```
java -jar /build/libs/musicapi-0.1.0.jar
```
- The application will run on port number 8080 and can be accessed using
[https://localhost:8080/albums/](http://localhost:8080/albums/) as the base context

### Option 2
- Clone the project from [github](https://github.com/rbidanta/musicapi)
- Make sure to install latest version of [gradle](http://gradle.org/install/) using the following command or any other package manager
```
brew install gradle
```
- Run the following command to build the project
```
gradle bootrun
```
- The application will run on port number 8080 and can be accessed using
[https://localhost:8080/albums/](https://localhost:8080/albums/) as the base context

