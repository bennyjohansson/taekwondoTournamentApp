# Taekwondo Tournament App

A Spring Boot application for managing Taekwondo tournaments, including participant registration, match scheduling, and tournament organization.

## Features

- **Tournament Management**
  - Create and manage tournaments
  - Configure tournament categories (age groups, gender, skill levels)
  - Set number of mats for tournaments

- **Participant Management**
  - Register participants with details (name, age, gender, skill level)
  - Associate participants with clubs
  - Track participant information

- **Club Management**
  - Register and manage Taekwondo clubs
  - Track club locations and members

- **Match Management**
  - Schedule matches with specific mat assignments
  - Track match results and winners
  - Support for different tournament rounds (Quarter Final, Semi Final, Final)

## Technical Stack

- Java 17
- Spring Boot 3.2.3
- PostgreSQL 16.2
- Maven
- JPA/Hibernate
- Lombok

## Prerequisites

- Java 17 or higher
- PostgreSQL 16.2 or higher
- Maven 3.6 or higher

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/bennyjohansson/taekwondoTournamentApp.git
   cd taekwondoTournamentApp
   ```

2. Create a PostgreSQL database:
   ```bash
   createdb taekwondo_tournament
   ```

3. Configure the application:
   - Copy `application.properties` to `application-local.properties`
   - Update database credentials in `application-local.properties` if needed

4. Build the project:
   ```bash
   mvn clean install
   ```

5. Run the application:
   ```bash
   mvn spring:boot run
   ```

The application will start on `http://localhost:8080`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/taekwondo/tournament/
│   │       ├── model/           # Domain models
│   │       ├── repository/      # JPA repositories
│   │       ├── service/         # Business logic
│   │       ├── controller/      # REST endpoints
│   │       └── TaekwondoTournamentApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/taekwondo/tournament/
            └── model/           # Test classes
```

## Domain Models

- **Tournament**: Represents a Taekwondo tournament with categories and mat configuration
- **Participant**: Represents a tournament participant with personal and skill information
- **Club**: Represents a Taekwondo club
- **Match**: Represents a match in the tournament with participants and results

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 