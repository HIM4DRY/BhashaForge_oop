# BhashaForge_oop

A Banglish mini compiler/interpreter with a simple menu-driven CLI and MySQL-backed
user accounts + saved code snippets.

## Features
- Custom lexer/parser/interpreter for a small Banglish language (`dhoro`, `dekhao`, `jodi`, `nahole`, `jotokkhon`)
- User registration/login
- Save, view, and delete code snippets per user (stored in MySQL)

## Requirements
- Java 17+ (or whatever JDK you're using in IntelliJ)
- MySQL / MariaDB (e.g. via XAMPP)
- MySQL Connector/J (JDBC driver) on the classpath

## Setup

1. **Start MySQL** (e.g. open XAMPP Control Panel and start MySQL).

2. **Create the database and tables.** Import the schema:
   ```bash
   mysql -u root -p < schema.sql
   ```
   Or open phpMyAdmin → SQL tab → paste the contents of `schema.sql` → Go.

   This creates a database named `bhashaforge_db` with two tables:
   - `users` (id, username, password)
   - `saved_codes` (id, user_id, title, code, saved_at)

3. **Check DB credentials** in `src/bhashaforge/storage/DBConnection.java`:
   ```java
   private static final String URL  = "jdbc:mysql://localhost:3306/bhashaforge_db";
   private static final String USER = "root";
   private static final String PASS = "";
   ```
   Update `USER`/`PASS` if your MySQL setup differs from the default XAMPP config.

4. **Add the MySQL JDBC driver** to your project (if not already):
   - Download `mysql-connector-j` from [Maven Central](https://mvnrepository.com/artifact/com.mysql/mysql-connector-j)
   - In IntelliJ: `File > Project Structure > Libraries > + > Java` → select the downloaded `.jar`

5. **Run** `Main.java`.

## Project Structure
```
bhashaforge/
├── ast/            # AST node classes
├── environment/    # Variable storage during interpretation
├── interpreter/    # Tree-walking interpreter
├── lexer/          # Tokenizer
├── parser/         # Recursive-descent parser
├── storage/        # DB connection + code save/load
├── token/          # Token + TokenType definitions
├── user/           # User model + UserDAO (login/register)
└── main/           # Entry point (Main.java) with CLI menu
```

## Note on security
The current version stores passwords in **plain text** and DB credentials are
hardcoded. This is fine for a learning project but should not be used as-is
in production — consider hashing passwords (e.g. BCrypt) and moving credentials
to environment variables or a config file excluded from git.
