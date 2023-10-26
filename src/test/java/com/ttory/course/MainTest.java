package com.ttory.course;

import com.ttory.course.components.CourseComponent;
import com.ttory.course.pages.BasePage;
import com.ttory.course.pages.CoursePage;
import com.ttory.course.pages.CoursesPage;
import com.ttory.course.pages.EventsPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Setter
public class MainTest {
    private static String driverType = null;

    @Getter
    private WebDriver webDriver;

    @Getter
    private static final Logger logger = LogManager.getLogger(MainTest.class);

    @Getter
    private boolean cookiePressed = false;


    @BeforeAll
    static void setupAll() {
        String driverTypeEnv = System.getProperty("driver");
        if (driverTypeEnv == null) {
            System.out.println("Please add `driver` environment variable to select webdriver!" +
                    "(or add for exmp -Ddriver=chrome)");
            getLogger().error("No `driver` environment present");
            System.exit(1);
        }
        driverType = driverTypeEnv;
        WebDriverManager.getInstance(driverType).setup();
    }
    @BeforeEach
    void setup() {
        String remoteServer = System.getProperty("remote_server");

        List<String> options = new ArrayList<>();
        options.add("--headless");

        WebDriver driver = WebDriverFactory.create(driverType, options, remoteServer);
        if (driver == null) {
            System.out.println("Please add `driver` environment variable to select webdriver!");
            getLogger().error("No `driver` environment present");
            System.exit(1);
        }
        setWebDriver(driver);
    }

    @AfterEach
    void teardown() {
        getWebDriver().quit();
    }

    private void removeCookieAlert(BasePage po) {
        if (!isCookiePressed()) {
            setCookiePressed(po.pressCookieButton());
        }

        po.waitPageLoad();
    }

    private CoursesPage loadCoursesPage() {
        CoursesPage coursesPage = new CoursesPage(getWebDriver());
        coursesPage.open();
        coursesPage.waitPageLoad();
        removeCookieAlert(coursesPage);
        coursesPage.waitPageLoad();
        coursesPage.clickTestDirCb();

        while (coursesPage.pressShowButton()) {
            getLogger().info("Clicking to show more button");
        }
        return coursesPage;
    }

    @Test
    void testCourses() {
        CoursesPage coursesPage = loadCoursesPage();
        coursesPage.testCount(10);
        int i = 0;

        for(CourseComponent course: coursesPage.getCourses()) {
            String link = course.getLink().getAttribute("href");
            String title = coursesPage.getTitle(i);
            try {
                Document doc = Jsoup.connect(link).get();
                CoursePage coursePage = new CoursePage(doc);
                coursePage.checkTitle(title);
                coursePage.hasDuration();
                coursePage.hasFormat();
                coursePage.hasDescription();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            i++;
        }
    }

    @Test
    void testEvents() {
        EventsPage eventsPage = new EventsPage(getWebDriver());
        eventsPage.open();
        eventsPage.waitPageLoad();
        removeCookieAlert(eventsPage);
        eventsPage.waitPageLoad();
        eventsPage.scrollDown();
        getLogger().info("Event count is {}", eventsPage.eventsCount());
        eventsPage.checkDates();
    }

    @Test
    void testOpenWebinars() {
        EventsPage eventsPage = new EventsPage(getWebDriver());
        eventsPage.open();
        eventsPage.waitPageLoad();
        removeCookieAlert(eventsPage);
        eventsPage.waitPageLoad();
        eventsPage.filterOpenWebinars();
        eventsPage.scrollDown();
        getLogger().info("Event count is {}", eventsPage.eventsCount());
        eventsPage.checkOpenWebinars("Открытый вебинар");
    }
}
