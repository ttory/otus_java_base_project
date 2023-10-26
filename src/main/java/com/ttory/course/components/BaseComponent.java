package com.ttory.course.components;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;

public abstract class BaseComponent {
    protected WebElement root;
    @Getter
    @Setter
    private Logger logger;

    public BaseComponent(WebElement root) {
        this.root = root;
        PageFactory.initElements(new DefaultElementLocatorFactory(root), this);
    }
}
