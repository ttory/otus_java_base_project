package com.ttory.course.pages;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    @Getter
    @Setter
    private WebDriver webDriver;
    @Getter
    @Setter
    private WebDriverWait waitObject;
    @Getter
    @Setter
    private Logger logger;
    @Getter
    @Setter
    private String pageUrl;
    @Getter
    @Setter
    private String baseUrl;

    @FindBy(xpath = "//span[contains(text(), 'Посещая наш сайт, вы принимаете')]/../div/button")
    private WebElement cookieButton;

    public BasePage(WebDriver driver) {
        String url = System.getProperty("base.url");
        if (url == null) {
            System.out.println("Please add `base.url` environment variable to select webdriver! " +
                    "(or add for exmp -Dbase.url=http://otus.ru)");
            getLogger().error("No `base.url` environment present");
            System.exit(1);
        }
        setBaseUrl(url);
        setWebDriver(driver);
        setWaitObject(new WebDriverWait(driver, Duration.ofSeconds(10)));
    }
    public boolean pressCookieButton() {
        try {
            getWaitObject().until(ExpectedConditions.elementToBeClickable(cookieButton));

            try {
                if (cookieButton.isDisplayed()) {
                    JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
                    executor.executeScript("arguments[0].click();", cookieButton);
                    logger.info("Cookie button pressed");
                    return true;
                }
            } catch (NoSuchElementException ignored) {
            }
        } catch (TimeoutException ignored) {
        }
        return false;
    }

    public void waitPageLoad() {
        getWaitObject().until( new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply (WebDriver driver){
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        });
    }

    public void open() {
        getWebDriver().get(getPageUrl());
    }
}
