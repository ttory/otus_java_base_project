package com.ttory.course.pages;

import com.ttory.course.components.EventComponent;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventsPage extends BasePage {
    private static final String OTUS_EVENTS_URL = "https://otus.ru/events/near/";
    @FindBy(xpath = "//a[@class = 'dod_new-event']")
    private List<WebElement> eventsList;
    @FindBy(xpath = "//div[contains(@class, 'dod_new-events-dropdown')]/div[@class = 'dod_new-events-dropdown__input']")
    private WebElement eventTypeSelector;
    @FindBy(xpath = "//div[contains(@class, 'dod_new-events-dropdown')]/div[@class = 'dod_new-events-dropdown__list js-dod_new_events-dropdown']/a[@class = 'dod_new-events-dropdown__list-item' and contains(text(), 'Открытый вебинар')]")
    private WebElement eventOpenWebinarSelector;
    @FindBy(xpath = "//div[contains(@class, 'dod_new-loader-wrapper')]")
    private WebElement loader;

    public EventsPage(WebDriver driver) {
        super(driver);
        setLogger(LogManager.getLogger(EventsPage.class));
        PageFactory.initElements(driver, this);
        setPageUrl(OTUS_EVENTS_URL);
    }

    public void scrollDown() {
        long lastHeight = (long) ((JavascriptExecutor) getWebDriver()).executeScript("return document.body.scrollHeight");

        getLogger().info("Scrolling page, H: {}", lastHeight);
        while (true) {
            ((JavascriptExecutor) getWebDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                getWaitObject().until(ExpectedConditions.visibilityOf(loader));
            } catch (TimeoutException ignored) {
            }
            try {
                getWaitObject().until(ExpectedConditions.invisibilityOf(loader));
            } catch (TimeoutException ignored) {
            }
            long newHeight = (long) ((JavascriptExecutor) getWebDriver()).executeScript("return document.body.scrollHeight");

            getLogger().info("Scrolling page, new H: {}", newHeight);
            if (newHeight == lastHeight) {
                break;
            }
            lastHeight = newHeight;
        }
    }

    public List<EventComponent> getEvents() {
        return eventsList.stream().map(EventComponent::new).collect(Collectors.toList());
    }

    public int eventsCount() {
        return eventsList.size();
    }

    public void filterOpenWebinars() {
        getWaitObject().until(ExpectedConditions.visibilityOf(eventTypeSelector));
        eventTypeSelector.click();
        getWaitObject().until(ExpectedConditions.visibilityOf(eventOpenWebinarSelector));
        JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
        executor.executeScript("arguments[0].click();", eventOpenWebinarSelector);
    }

    public void checkDates() {
        Date date = java.util.Date.from(LocalDate.now().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        for (EventComponent el : getEvents()) {
            el.checkDate(date);
        }
    }

    public void checkOpenWebinars(String type) {
        for (EventComponent el : getEvents()) {
            el.checkType(type);
        }
    }

}
