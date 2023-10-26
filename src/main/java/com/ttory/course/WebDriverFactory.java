package com.ttory.course;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.List;

public class WebDriverFactory {
    static public WebDriver create(String type, List<String> options, String remoteServer) {
        if (type == null) {
            return null;
        }

        String driverType = type.trim().toUpperCase();

        WebDriverManager manager = WebDriverManager.getInstance(driverType);

        if (DriverManagerType.CHROME.toString().equals(driverType)) {
            ChromeOptions chromeOptions = new ChromeOptions();
            if (options != null) {
                for (String option: options) {
                    chromeOptions.addArguments(option);
                }
            }
            chromeOptions.addArguments("--remote-allow-origins=*");
            manager.capabilities(chromeOptions);
        } else if (DriverManagerType.FIREFOX.toString().equals(driverType)) {
            FirefoxOptions ffOptions = new FirefoxOptions();
            if (options != null) {
                for (String option: options) {
                    ffOptions.addArguments(option);
                }
            }
            manager.capabilities(ffOptions);
        }
        if (remoteServer != null) {
            manager.remoteAddress(remoteServer);
        }
        WebDriver driver = manager.create();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        return driver;
    }
}
