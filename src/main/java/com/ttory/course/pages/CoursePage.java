package com.ttory.course.pages;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Getter
public class CoursePage {
    private static final String SVG_DURATION = "M17.1604 2.76862L17.1614 3.51824C19.916 3.73413 21.7357 5.61119 21.7386 8.48975L21.7495 16.9155C21.7534 20.054 19.7817 21.985 16.6213 21.99L8.9014 22C5.76071 22.004 3.76433 20.027 3.76038 16.8795L3.74952 8.55272C3.74557 5.65517 5.50104 3.78311 8.25568 3.53024L8.2547 2.78061C8.25371 2.34083 8.57953 2.01 9.01395 2.01C9.44838 2.009 9.77419 2.33883 9.77518 2.77861L9.77617 3.47826L15.6409 3.47027L15.6399 2.77062C15.6389 2.33084 15.9647 2.001 16.3992 2C16.8237 1.999 17.1594 2.32884 17.1604 2.76862ZM5.27099 8.86157L20.2191 8.84158V8.49175C20.1767 6.34283 19.0985 5.21539 17.1634 5.04748L17.1643 5.81709C17.1643 6.24688 16.8296 6.5877 16.4051 6.5877C15.9707 6.5887 15.6439 6.24887 15.6439 5.81909L15.6429 5.0095L9.77814 5.01749L9.77913 5.82609C9.77913 6.25687 9.4543 6.5967 9.01987 6.5967C8.58545 6.5977 8.25865 6.25887 8.25865 5.82809L8.25766 5.05847C6.33237 5.25137 5.26704 6.38281 5.27 8.55072L5.27099 8.86157ZM15.9894 13.4043V13.4153C15.9993 13.8751 16.3745 14.2239 16.8296 14.2139C17.2739 14.2029 17.6284 13.8221 17.6185 13.3623C17.5978 12.9225 17.2414 12.5637 16.798 12.5647C16.3439 12.5747 15.9884 12.9445 15.9894 13.4043ZM16.805 17.892C16.3508 17.882 15.9845 17.5032 15.9835 17.0435C15.9736 16.5837 16.3379 16.2029 16.7921 16.1919H16.802C17.266 16.1919 17.6422 16.5707 17.6422 17.0405C17.6432 17.5102 17.268 17.891 16.805 17.892ZM11.9216 13.4203C11.9414 13.8801 12.3175 14.2389 12.7717 14.2189C13.216 14.1979 13.5705 13.8181 13.5507 13.3583C13.5399 12.9085 13.1746 12.5587 12.7303 12.5597C12.2761 12.5797 11.9206 12.9605 11.9216 13.4203ZM12.7757 17.8471C12.3215 17.8671 11.9463 17.5082 11.9256 17.0485C11.9256 16.5887 12.28 16.2089 12.7342 16.1879C13.1785 16.1869 13.5448 16.5367 13.5547 16.9855C13.5754 17.4463 13.22 17.8261 12.7757 17.8471ZM7.85384 13.4553C7.87359 13.915 8.24976 14.2749 8.70393 14.2539C9.14823 14.2339 9.50268 13.8531 9.48194 13.3933C9.47207 12.9435 9.10676 12.5937 8.66147 12.5947C8.2073 12.6147 7.85285 12.9955 7.85384 13.4553ZM8.70788 17.8521C8.25371 17.8731 7.87852 17.5132 7.85779 17.0535C7.8568 16.5937 8.21224 16.2129 8.66641 16.1929C9.11071 16.1919 9.47701 16.5417 9.48688 16.9915C9.50761 17.4513 9.15316 17.8321 8.70788 17.8521Z";
    private static final String SVG_FORMAT = "M3.90039 10.3178C3.90039 5.71789 7.74427 2 12.3938 2C17.0565 2 20.9004 5.71789 20.9004 10.3178C20.9004 12.6357 20.0574 14.7876 18.6699 16.6116C17.1392 18.6235 15.2525 20.3765 13.1289 21.7524C12.6429 22.0704 12.2043 22.0944 11.6708 21.7524C9.53513 20.3765 7.64848 18.6235 6.13089 16.6116C4.74237 14.7876 3.90039 12.6357 3.90039 10.3178ZM9.59462 10.5768C9.59462 12.1177 10.852 13.3297 12.3938 13.3297C13.9366 13.3297 15.2062 12.1177 15.2062 10.5768C15.2062 9.0478 13.9366 7.77683 12.3938 7.77683C10.852 7.77683 9.59462 9.0478 9.59462 10.5768Z";

    private static final String TEXT_XPATH = "/text()";

    @Setter
    private Document document;

    @Setter
    Logger logger;

    private final String COURSE_TITLE_SELECTOR = "div[class*=\"sc-s2pydo-6\"] > h1";

    private final String COURSE_DURATION_SELECTOR = "div:has(> div > svg > path[d='" + SVG_DURATION + "']) + p";
    private final String COURSE_FORMAT_SELECTOR = "div:has(> div > svg > path[d='" + SVG_FORMAT + "']) + p";
    private final List<String> COURSE_DESCRIPTION_SELECTOR_LIST = Arrays.asList(
            "div[class='sc-x072mc-0 sc-1oat6ot-0 hOtCic dYVcmu']",
            "div[class='sc-1og4wiw-0 sc-pyhrzd-0 jQNgtj gaFmBc']"
    );

    public CoursePage(Document doc) {
        setDocument(doc);
        setLogger(LogManager.getLogger(CoursePage.class));
    }

    private String getTitle() {
        String title = null;
        try {
            Elements el = getDocument().select(COURSE_TITLE_SELECTOR);
            if (el.hasText()) {
                title = el.text();
                return title;
            }
        } finally {
            getLogger().info(String.format("Course title: %s", title));
        }
        return null;
    }

    private String getElement(String selector) {
        String text = null;
        try {
            Elements el = getDocument().select(selector);
            if (!el.isEmpty()) {
                text = el.text();
                return text;
            }
        } finally {
            getLogger().info(String.format("Text: %s", text));
        }
        return null;
    }

    private void hasElement(String selector) {
        String text = getElement(selector);
        assertNotEquals(null, text, "No text found");
    }

    private void checkDescription() {
        for (String selector: COURSE_DESCRIPTION_SELECTOR_LIST) {
            if (getElement(selector) != null) {
                return;
            }
        }
        fail("No description");
    }

    public void checkTitle(String title) {
        assertEquals(title, getTitle(), "Incorrect course title");
    }

    public void hasDuration() {
        getLogger().info("Checking duration");
        hasElement(COURSE_DURATION_SELECTOR);
    }

    public void hasFormat() {
        getLogger().info("Checking format");
        hasElement(COURSE_FORMAT_SELECTOR);
    }

    public void hasDescription() {
        getLogger().info("Checking description");
        checkDescription();
    }
}
