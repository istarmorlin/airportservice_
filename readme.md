# Airport service application

## Setup

### Prerequisites

- Java11 (OpenJDK 11 for instance)
- MySQL server

IDE should also have [Lombok](https://projectlombok.org/) plugin installed and annotation processing feature enabled.

### Environment variables
In order to start the application in local environment, following environment variables need to be set:

``spring.profiles.active`` = ``dev``

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
    "code" : <flight_code>
}
```

Example body:
```
{
    "code" : "FL2"
}
```

Request URI `PATCH` ``/gates/{gateName}``

Request body:
```
{
    "flightDto" : {
        "code" : <flight_code>
    }
}
```

Example URI: `PATCH` ``/gates/A1``

Example body:
```
{
    "flightDto" : {
        "code" : "FL2"
    }
}
```