package com.ttory.course.components;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Getter
public class EventComponent extends BaseComponent {
    public static final String[] MONTH_RUS = {
            "января", "февраля", "марта", "апреля",
            "мая", "июня", "июля", "августа",
            "сентября", "октября", "ноября", "декабря"
    };
    public static final String[] MONTH_INDEX = {
            "01", "02", "03", "04",
            "05", "06", "07", "08",
            "09", "10", "11", "12"
    };
    public static final String DATE_FORMAT = "dd MM yyyy";

    @FindBy(xpath = ".//span[@class = 'dod_new-event__date-text']")
    WebElement date;

    @FindBy(xpath = ".//div[@class = 'dod_new-type__text']")
    WebElement type;

    public EventComponent(WebElement root) {
        super(root);
        setLogger(LogManager.getLogger(EventComponent.class));
    }

    private static String replaceAll(String src, String[] replace, String[] by) {
        for (int i = 0; i < replace.length; i++) {
            src = src.replace(replace[i], by[i]);
        }
        return src;
    }

    public void checkType(String type) {
        assertEquals(getType().getText(), type, "Incorrect type!");
    }

    public void checkDate(Date date) {
        Date parsingDate;
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);
        try {
            parsingDate = ft.parse(replaceAll(getDate().getText(),
                    MONTH_RUS,
                    MONTH_INDEX) + " " + Calendar.getInstance().get(Calendar.YEAR));
            getLogger().info(parsingDate);
        } catch (ParseException e) {
            getLogger().info("Failed to get data for " + getDate().getText());
            fail("Failed to get data for " + getDate().getText());
            return;
        }
        assertFalse(parsingDate.compareTo(date) < 0, "Outdated course!");
    }
}
