package com.ttory.course;

import com.ttory.course.po.AbstractOtusPage;
import com.ttory.course.po.CoursePage;
import com.ttory.course.po.CoursesPage;
import com.ttory.course.po.EventsPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {
    public static final String CHROME_DRIVER_VERSION = "116.0.5845.96";
    public static final String OTUS_COURSES_URL = "https://otus.ru/catalog/courses";
    public static final String OTUS_EVENTS_URL = "https://otus.ru/events/near/";
    static String driverType = null;
    WebDriver driver;
    final Logger logger = LogManager.getLogger(MainTest.class);
    private boolean cookiePressed = false;
    private boolean chatPressed = false;

    @BeforeAll
    static void setupAll() {
        String driverTypeEnv = System.getProperty("driver");
        if (driverTypeEnv == null) {
            System.out.println("Please add `driver` environment variable to select webdriver!");
            System.exit(1);
        }
        driverType = driverTypeEnv;
        WebDriverManager.getInstance(driverType).setup();
    }
    @BeforeEach
    void setup() {
        String remoteServer = System.getProperty("remote_server");
        if (driverType == null) {
            System.out.println("Please add `driver` environment variable to select webdriver!");
            System.exit(1);
        }

        String type = driverType.trim().toUpperCase();
        WebDriverManager manager = WebDriverManager.getInstance(type);
        if (DriverManagerType.CHROME.toString().equals(type)) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--remote-allow-origins=*");
            chromeOptions.addArguments("--headless");
            manager.driverVersion(CHROME_DRIVER_VERSION).capabilities(chromeOptions);
        } else if (DriverManagerType.FIREFOX.toString().equals(type)) {
            FirefoxOptions ffOptions = new FirefoxOptions();
            ffOptions.addArguments("--headless");
            manager.capabilities(ffOptions).create();
        }
        if (remoteServer != null) {
            manager.remoteAddress(remoteServer);
        }
        driver = manager.create();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    private void removeCookieAndChat(AbstractOtusPage po) {
        if (!cookiePressed) {
            cookiePressed = po.pressCookieButton();
        }
        if (!chatPressed) {
            chatPressed = po.pressChatButton();
        }
        po.addWait();
    }

    private CoursesPage loadCoursesPage() {
        CoursesPage coursesPage = new CoursesPage(driver);
        coursesPage.addWait();
        removeCookieAndChat(coursesPage);
        coursesPage.addWait();
        coursesPage.clickTestDirCb();

        while (coursesPage.pressShowButton()) {
            logger.info("Clicking to show more button");
        }
        return coursesPage;
    }


    @Test
    void testCourses() {
        driver.get(OTUS_COURSES_URL);
        CoursesPage coursesPage = loadCoursesPage();
        assertEquals(12, coursesPage.getCoursesCount(), "Courses count");


        for (int i = 0; i < coursesPage.getCoursesCount(); i++) {
            String title = coursesPage.getTitle(i);
            logger.info(String.format("Cheking course %d: %s", i + 1, title));
            coursesPage.clickOnCourse(i);
            CoursePage coursePage = new CoursePage(driver);
            coursePage.addWait();
            assertEquals(title, coursePage.getTitle(), "Course title");
            assertTrue(coursePage.hasDuration(), "Course duration");
            assertTrue(coursePage.hasFormat(), "Course format");
            assertTrue(coursePage.hasDescription(), "Course description");
            driver.navigate().back();
            coursesPage = loadCoursesPage();
        }
    }

    @Test
    void testEvents() {
        driver.get(OTUS_EVENTS_URL);

        EventsPage eventsPage = new EventsPage(driver);
        eventsPage.addWait();
        removeCookieAndChat(eventsPage);
        eventsPage.addWait();
        eventsPage.scrollDown();
        logger.info("Event count is {}", eventsPage.eventsCount());
        assertTrue(eventsPage.checkDates(), "Outdated course!");
    }

    @Test
    void testOpenWebinars() {
        driver.get(OTUS_EVENTS_URL);

        EventsPage eventsPage = new EventsPage(driver);
        eventsPage.addWait();
        removeCookieAndChat(eventsPage);
        eventsPage.addWait();
        eventsPage.filterOpenWebinars();
        eventsPage.scrollDown();
        logger.info("Event count is {}", eventsPage.eventsCount());
        assertTrue(eventsPage.checkOpenWebinars("Открытый вебинар"), "Incorrect type!");
    }
}
