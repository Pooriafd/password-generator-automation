package com.security.passwordgenerator.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {
    static WebDriver driver;

    public static WebDriver initiate() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.security.org/password-generator/");
        return driver;
    }

    public static void terminate() {
        if (driver != null) {
            driver.quit();
        }
    }
}
