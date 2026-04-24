# Library System

Console-based Java application for a library database project.

The system uses MySQL and JDBC. It has separate menu flows for readers and librarians.

## Main Features

### Reader

- Browse and search books.
- Log in and log out.
- View profile.
- Update profile.
- Change password.
- View active loans.
- View loan history.
- Borrow, return and extend loans.

### Librarian

- Manage members.
- Manage authors.
- View and search books.
- Add books.
- View active loans.
- Create loans.
- Register returned loans.

## Project Structure

```text
src/
  Main.java      Application entry point
  ui/            Console menu, auth and printing
  member/        Member logic
  book/          Book logic
  loan/          Loan logic
  author/        Author logic
  category/      Category logic
  base/          Shared base classes
  assets/        SQL script, database example file and MySQL driver
  docs/          Requirements, ERD and menu sketch
```

The project follows a simple layered structure:

```text
Controller -> Service -> Repository
```

DTOs and mappers are used to separate database entities from data shown in the console.

## Database Setup

The database script is here:

```text
src/assets/generate-library.sql
```

Run this script in MySQL to create the `library` database and example data.

Create a `database.properties` file on the application classpath:

```properties
DB_URL=jdbc:mysql://localhost:3306/library
DB_USERNAME=root
DB_PASSWORD=your_password
```

Example file:

```text
src/assets/example-database.properties
```

Do not commit your real `database.properties` file.

## How To Run

This project does not use Maven or Gradle yet. Run it as a plain Java project in IntelliJ IDEA.

Steps:

1. Start MySQL.
2. Run `src/assets/generate-library.sql`.
3. Create `database.properties`.
4. Open the project in IntelliJ IDEA.
5. Configure a JDK.
6. Run `src/Main.java`.

## Current Limitations

- No Maven or Gradle build file.
- No automated tests yet.
- Not a Spring Boot application yet.
- Some repository methods are incomplete.
- Passwords are currently stored as plain text.
- Roles and statuses are currently represented as strings.

## Future Improvements

- Add Maven or Gradle.
- Add JUnit tests.
- Improve exception handling.
- Use password hashing.
- Use enums for roles and statuses.
- Consider migrating to Spring Boot.
