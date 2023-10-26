package com.ttory.course.components;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class CourseComponent extends BaseComponent {

    @FindBy(xpath = ".")
    WebElement link;
    public CourseComponent(WebElement root) {
        super(root);
        setLogger(LogManager.getLogger(EventComponent.class));
    }
}
