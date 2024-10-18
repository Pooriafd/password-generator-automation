package com.security.passwordgenerator.testcase;

import com.security.passwordgenerator.config.DriverManager;
import com.security.passwordgenerator.page.PasswordGeneratorPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashSet;
import java.util.Set;

public class PasswordGeneratorTest {
    PasswordGeneratorPage passwordPage;
    WebDriver driver;
    private static final int DEFAULT_LENGTH = 8;
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 32;

    @BeforeClass
    public void setup() {
        driver = DriverManager.initiate();
        passwordPage = new PasswordGeneratorPage(driver);
    }

    @Test
    public void testGeneratePasswordWithDefaultSettings() {
        passwordPage.clickGenerateButton();
        String password = passwordPage.getGeneratedPassword();
        Assert.assertNotNull(password, "Password should not be null.");
    }

    @Test
    public void testGeneratePasswordWithSpecificLength() {
        passwordPage.dragToLength(DEFAULT_LENGTH);
        passwordPage.clickGenerateButton();
        String password = passwordPage.getGeneratedPassword();
        Assert.assertEquals(password.length(), DEFAULT_LENGTH, "Password length should be 8.");
    }

    @Test
    public void testGeneratePasswordWithAllConditions() {
        // Select all options
        passwordPage.selectAllOptions();

        // Set the password length (e.g., 12)
        passwordPage.dragToLength(12);

        // Click the generate button
        passwordPage.clickGenerateButton();

        // Get the generated password
        String password = passwordPage.getGeneratedPassword();

        // Assert that the password is not null
        Assert.assertNotNull(password, "Password should not be null.");

        // Validate password contains at least one lowercase, one uppercase, one number, and one special character
        Assert.assertTrue(passwordPage.containsLowerCase(password), "Password should contain at least one lowercase letter.");
        Assert.assertTrue(passwordPage.containsUpperCase(password), "Password should contain at least one uppercase letter.");
        Assert.assertTrue(passwordPage.containsNumber(password), "Password should contain at least one number.");
        Assert.assertTrue(passwordPage.containsSpecialCharacter(password), "Password should contain at least one special character.");
    }

    @Test
    public void testGenerateLowercaseLettersOnly() {
        // Set password length (e.g., 8 characters)
        passwordPage.dragToLength(DEFAULT_LENGTH);

        // Uncheck uppercase, numbers, and symbols options
        passwordPage.checkAndSelect(passwordPage.uppercaseCheckbox, passwordPage.uppercaseLabel, false);
        passwordPage.checkAndSelect(passwordPage.numbersCheckbox, passwordPage.numbersLabel, false);
        passwordPage.checkAndSelect(passwordPage.symbolsCheckbox, passwordPage.symbolsLabel, false);

        // Select lowercase option
        passwordPage.checkAndSelect(passwordPage.lowercaseCheckbox, passwordPage.lowercaseLabel, true);

        // Click the generate password button
        passwordPage.clickGenerateButton();

        // Get the generated password
        String generatedPassword = passwordPage.getGeneratedPassword();

        // Validate that the password contains only lowercase letters
        Assert.assertTrue(passwordPage.containsLowerCase(generatedPassword),
                "Password does not contain lowercase letters.");
        Assert.assertFalse(passwordPage.containsUpperCase(generatedPassword),
                "Password contains uppercase letters.");
        Assert.assertFalse(passwordPage.containsNumber(generatedPassword),
                "Password contains numbers.");
        Assert.assertFalse(passwordPage.containsSpecialCharacter(generatedPassword),
                "Password contains special characters.");
    }

    @Test
    public void testGenerateNumbersOnly() {
        // Set password length (e.g., 8 characters)
        passwordPage.dragToLength(DEFAULT_LENGTH);

        // Select numbers option
        passwordPage.checkAndSelect(passwordPage.numbersCheckbox, passwordPage.numbersLabel, true);

        // Uncheck lowercase, uppercase, and symbols options
        passwordPage.checkAndSelect(passwordPage.uppercaseCheckbox, passwordPage.uppercaseLabel, false);
        passwordPage.checkAndSelect(passwordPage.symbolsCheckbox, passwordPage.symbolsLabel, false);
        passwordPage.checkAndSelect(passwordPage.lowercaseCheckbox, passwordPage.lowercaseLabel, false);

        // Click the generate password button
        passwordPage.clickGenerateButton();

        // Get the generated password
        String generatedPassword = passwordPage.getGeneratedPassword();

        // Validate that the password contains only numbers
        Assert.assertTrue(passwordPage.containsNumber(generatedPassword),
                "Password does not contain numbers.");
        Assert.assertFalse(passwordPage.containsLowerCase(generatedPassword),
                "Password contains lowercase letters.");
        Assert.assertFalse(passwordPage.containsUpperCase(generatedPassword),
                "Password contains uppercase letters.");
        Assert.assertFalse(passwordPage.containsSpecialCharacter(generatedPassword),
                "Password contains special characters.");
    }

    @Test
    public void testNoOptionsSelected() {
        // Set password length (e.g., 8 characters)
        passwordPage.dragToLength(DEFAULT_LENGTH);


        // Uncheck ALL
        passwordPage.checkAndSelect(passwordPage.uppercaseCheckbox, passwordPage.uppercaseLabel, false);
        passwordPage.checkAndSelect(passwordPage.symbolsCheckbox, passwordPage.symbolsLabel, false);
        passwordPage.checkAndSelect(passwordPage.numbersCheckbox, passwordPage.numbersLabel, false);
        passwordPage.checkAndSelect(passwordPage.lowercaseCheckbox, passwordPage.lowercaseLabel, false);

        Assert.assertTrue(passwordPage.getSelectValue(passwordPage.lowercaseCheckbox),
                "At least one option should be selected");
    }

    @Test
    public void testMinimumLengthViolation() {
        passwordPage.clearLength();
        Assert.assertEquals(passwordPage.getGeneratedPassword().length(), MIN_LENGTH, "Minimum length is not set correctly");
        passwordPage.setLength("90");
        Assert.assertEquals(passwordPage.getGeneratedPassword().length(), MAX_LENGTH, "Maximum length is not set correctly");
    }
    
    @Test
    public void testUniquenessCheck() {
        // Set password length
        passwordPage.dragToLength(DEFAULT_LENGTH); // Example length

        // Select all options to include various character types
        passwordPage.selectAllOptions();

        // Set to store generated passwords for uniqueness check
        Set<String> generatedPasswords = new HashSet<>();

        // Generate multiple passwords and store them
        int numberOfPasswordsToGenerate = 10; // Number of passwords to generate
        for (int i = 0; i < numberOfPasswordsToGenerate; i++) {
            passwordPage.clickGenerateButton(); // Generate password
            String generatedPassword = passwordPage.getGeneratedPassword(); // Retrieve the generated password
            generatedPasswords.add(generatedPassword); // Add to set
        }

        // Assert that the number of unique passwords equals the number generated
        Assert.assertEquals(generatedPasswords.size(), numberOfPasswordsToGenerate,
                "Not all generated passwords are unique. Expected unique count: " + numberOfPasswordsToGenerate +
                        ", but got: " + generatedPasswords.size());
    }

    @AfterClass
    public void tearDown() {
        DriverManager.terminate();
    }
}