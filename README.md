# Password Generator Automation

This project automates test cases for the password generator feature on https://www.security.org/password-generator/.

## Prerequisites
- Java 8 or higher
- Maven
- Chrome Browser
- WebDriverManager (included in the project)

## Setup Instructions
1. Clone the project.
2. Navigate to the project directory.
3. Run `mvn clean install` to build the project and download dependencies.

## Running Tests
To run the tests locally, execute the following command:
```
mvn test
```

The test results will be displayed in the console.

## Test Scenarios

### Happy Path Scenarios
The following scenarios ensure that the password generator functions correctly under expected conditions:

1. **Default Settings**: Generate a password with the default settings (no specific options selected).
2. **Specific Length**: Generate a password of a specific length (e.g., 8 characters).
3. **All Conditions**: Generate a password that includes all conditions (uppercase, lowercase, numbers, and special characters).
4. **Lowercase Letters Only**: Generate a password containing only lowercase letters.
5. **Numbers Only**: Generate a password with numbers only.
6. **Copy button**: Press Copy button put generated password in clipboard.

### Edge Cases / Negative Test Scenarios
These scenarios aim to validate the password generator's behavior under invalid conditions:

1. **Minimum Length Violation**: Attempt to generate a password with a length below the minimum (e.g., < 6 characters).
2. **Maximum Length Violation**: Attempt to generate a password with a length exceeding the maximum (e.g., > 32 characters).
3. **No Options Selected**: Generate a password with no options selected, ensuring that the page prevents this action.
4. **Uniqueness Check**: Generate multiple passwords with the same criteria and compare them to ensure they are unique.

