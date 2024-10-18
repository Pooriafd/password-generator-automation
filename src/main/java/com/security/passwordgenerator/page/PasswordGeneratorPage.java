package com.security.passwordgenerator.page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordGeneratorPage {
    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By generateButton = By.xpath("//button[@title='Generate password']");
    By copyButton = By.xpath("//button[@title='Copy password']");

    By passwordOutput = By.id("password");
    By lengthSlider = By.id("passwordLengthRange");
    By lengthInput = By.id("passwordLength");


    // Checkbox locators
    public By lowercaseCheckbox = By.id("option-lowercase");
    public By uppercaseCheckbox = By.id("option-uppercase");
    public By numbersCheckbox = By.id("option-numbers");
    public By symbolsCheckbox = By.id("option-symbols");

    // Label locators
    public By lowercaseLabel = By.xpath("//label[@for='option-lowercase']");
    public By uppercaseLabel = By.xpath("//label[@for='option-uppercase']");
    public By numbersLabel = By.xpath("//label[@for='option-numbers']");
    public By symbolsLabel = By.xpath("//label[@for='option-symbols']");

    By passwordLengthLabel = By.xpath("//label[@for='passwordLength']");

    public PasswordGeneratorPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // 10 seconds wait for each element

        // Wait for elements to be ready
        waitForElements();
    }

    // Method to copy the password
    public void copyPassword() {
        driver.findElement(copyButton).click();
    }

    // Method to wait for all elements to be present before interacting
    private void waitForElements() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(generateButton));
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordOutput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(lengthSlider));
    }

    public void clearLength() {
        driver.findElement(lengthInput).clear();
        driver.findElement(passwordLengthLabel).click();
    }

    public void setLength(String length) {
        driver.findElement(lengthInput).sendKeys(length);
        driver.findElement(passwordLengthLabel).click();
    }

    public void dragToLength(int length) {
        // Ensure the length is within the valid range
        if (length < 6 || length > 32) {
            throw new IllegalArgumentException("Length must be between 6 and 32.");
        }

        // Locate the slider and numeric input elements
        WebElement slider = driver.findElement(lengthSlider);

        // Get the current value of the slider to calculate the new position
        int currentValue = Integer.parseInt(Objects.requireNonNull(slider.getAttribute("value")));
        int delta = length - currentValue; // Calculate the difference to move

        // Drag the slider based on the delta
        // Each step should correspond to a change in the slider's value
        for (int i = 0; i < Math.abs(delta); i++) {
            if (delta > 0) {
                slider.sendKeys(Keys.ARROW_RIGHT);
            } else {
                slider.sendKeys(Keys.ARROW_LEFT);
            }
        }

    }

    // Click on the generate password button
    public void clickGenerateButton() {
        driver.findElement(generateButton).click();
    }

    // Get the generated password
    public String getGeneratedPassword() {
        return driver.findElement(passwordOutput).getAttribute("value");
    }

    // Select all password options
    public void selectAllOptions() {
        checkAndSelect(lowercaseCheckbox, lowercaseLabel);
        checkAndSelect(uppercaseCheckbox, uppercaseLabel);
        checkAndSelect(numbersCheckbox, numbersLabel);
        checkAndSelect(symbolsCheckbox, symbolsLabel);
    }

    // Helper method to check and select a checkbox by clicking its associated label
    public void checkAndSelect(By checkboxLocator, By labelLocator, boolean select) {
        WebElement checkbox = driver.findElement(checkboxLocator);
        if (checkbox.isSelected() != select) {
            // Click on the associated label
            WebElement label = driver.findElement(labelLocator);
            label.click();
        }
    }

    public boolean getSelectValue(By checkboxLocator) {
        WebElement checkbox = driver.findElement(checkboxLocator);
        return checkbox.isSelected();
    }

    private void checkAndSelect(By checkboxLocator, By labelLocator) {
        checkAndSelect(checkboxLocator, labelLocator, true);
    }

    // Validate password criteria
    public boolean containsLowerCase(String password) {
        Pattern pattern = Pattern.compile("[a-z]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public boolean containsUpperCase(String password) {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public boolean containsNumber(String password) {
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public boolean containsSpecialCharacter(String password) {
        Pattern pattern = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public String getClipboardContents() throws AWTException, IOException, UnsupportedFlavorException {
        // Use AWT to retrieve text from the clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        return (String) clipboard.getData(DataFlavor.stringFlavor);
    }
}
