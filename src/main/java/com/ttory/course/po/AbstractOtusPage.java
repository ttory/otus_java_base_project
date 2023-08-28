package com.ttory.course.po;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class AbstractOtusPage {
    final WebDriver driver;
    final WebDriverWait waitTen;
    final WebDriverWait waitOne;
    final WebDriverWait waitThirty;
    Logger logger;

    @FindBy(xpath = "//span[contains(text(), 'Посещая наш сайт, вы принимаете')]/../div/button")
    private WebElement cookieButton;
    @FindBy(xpath = "//jdiv[@class = 'closeIcon_a74e']")
    private WebElement chatButton;

    public AbstractOtusPage(WebDriver driver) {
        this.driver = driver;
        waitThirty = new WebDriverWait(driver, Duration.ofSeconds(30));
        waitTen = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitOne = new WebDriverWait(driver, Duration.ofSeconds(1));
    }

    public boolean pressCookieButton() {
        try {
            waitTen.until(ExpectedConditions.elementToBeClickable(cookieButton));

            try {
                if (cookieButton.isDisplayed()) {
                    JavascriptExecutor executor = (JavascriptExecutor) driver;
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

    public boolean pressChatButton() {
        try {
            waitOne.until(ExpectedConditions.elementToBeClickable(chatButton));

            try {
                if (chatButton.isDisplayed()) {
                    chatButton.click();
                    logger.info("Chat button pressed");
                    return true;
                }
            } catch (NoSuchElementException ignored) {
            }
        } catch (TimeoutException ignored) {
        }
        return false;
    }

    public void addWait() {
        waitThirty.until( new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply (WebDriver driver){
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        });
    }
}
