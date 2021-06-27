# Airport service application

## Setup

### Prerequisites

- Java11 (OpenJDK 11 for instance)
- MySQL server

IDE should also have [Lombok](https://projectlombok.org/) plugin installed and annotation processing feature enabled.

### Environment variables
In order to start the application in local environment, following environment variables need to be set:

``spring.profiles.active`` = ``dev``

Database connection uses default MySQL username and password (username: ```root```, password: ``` ```). If these differ on your local environment, please make such user exists or make adjustment in ``application-dev.properies`` file.

## Usage

### Structure

The application consists of three endpoints:

- `GET` ``/gates`` for fetching all gates
- `POST` ``/gates`` for assigning a flight to a gate
- `PATCH` ``/gates/{gateName}`` for updating gate flight information

### Minimal request body schema

Here you can find minimal request schema for these three operations.


Request URI: `GET` ``/gates``

Request body:
```
{

}
```

Request URI: `POST` ``/gates``

Request body:
```
{
    "code" : <flight_code>,
    "arrival" : <arival_date_and_time>,
    "nextDeparture" : <departure_date_and_time>
}
```

Example body:
```
{
    "code" : "FL2",
    "arrival" : "2021-06-25T05:31:00",
    "nextDeparture" : "2021-06-25T12:00:00"
}
```

Request URI `PATCH` ``/gates/{gateName}``

Request body:
```
{
    "flightDtos": 
    [
        {
            "code" : <flight_code>,
            "arrival" : <arival_date_and_time>,
            "nextDeparture" : <departure_date_and_time>
        }
    ]
}
```

Example URI: `PATCH` ``/gates/A1``

Example body:
```
{
    "flightDtos": 
    [
        {
            "code" : "FL2",
            "arrival" : "2021-06-25T05:31:00",
            "nextDeparture" : "2021-06-25T12:00:00"
        }
    ]
}
```