package com.ttory.course.pages;

import com.ttory.course.components.CourseComponent;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoursesPage extends BasePage {
    private static final String OTUS_COURSES_URL = "/catalog/courses";
    @FindBy(xpath = "//label[contains(text(), 'Тестирование')]")
    private WebElement testDirCheckbox;
    @FindBy(xpath = "//div[contains(text(), 'Каталог')]/../../../div/div/a")
    private List<WebElement> coursesList;
    @FindBy(xpath = "//button[contains(text(), 'Показать еще')]")
    private WebElement showMoreCoursesButton;

    public CoursesPage(WebDriver driver) {
        super(driver);
        setLogger(LogManager.getLogger(CoursesPage.class));
        PageFactory.initElements(driver, this);
        setPageUrl(getBaseUrl() + OTUS_COURSES_URL);
    }


    public List<CourseComponent> getCourses() {
        return coursesList.stream().map(CourseComponent::new).collect(Collectors.toList());
    }

    public void clickTestDirCb() {
        getWaitObject().until(ExpectedConditions.visibilityOf(testDirCheckbox));
        if (!testDirCheckbox.findElement(By.xpath("./../div/input")).isSelected()) {
            testDirCheckbox.click();
        }
    }

    public boolean pressShowButton() {
        try {
            try {
                getWaitObject().until(ExpectedConditions.visibilityOf(showMoreCoursesButton));
            } catch (TimeoutException e) {
                return false;
            }
            try {
                if (showMoreCoursesButton.isDisplayed()) {
                    showMoreCoursesButton.click();
                }
                return showMoreCoursesButton.isDisplayed();
            } catch (NoSuchElementException e) {
                return false;
            }
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    public int getCoursesCount() {
        return coursesList.size();
    }

    public void clickOnCourse(int i) {
        if (i < coursesList.size()) {
            coursesList.get(i).click();
        }
    }

    public String getTitle(int i) {
        if (i < coursesList.size()) {
            try {
                return coursesList.get(i).findElement(By.xpath(".//h6/div")).getText();
            } catch (NoSuchElementException e) {
                return null;
            }
        }
        return null;
    }

    public void testCount(int count) {
        assertEquals(count, getCoursesCount(), "Courses count");
    }
}
