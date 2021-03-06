package com.wiley.autotest.selenium;

import org.slf4j.LoggerFactory;
import org.testng.Reporter;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * Transfers message to necessary end point
 * TestNG
 * Allure
 * Jenkins console
 */
public class Report {
    private String message;

    public Report(String message) {
        this.message = message;
    }

    public void testNG() {
        Reporter.log(message + "<br/>");
    }

    public void allure() {
        allure(message);
    }

    /**
     * hack to send message to allure
     */
    @Step("{0}")
    private void allure(String message) {
    }

    /**
     * Making it an 'error' to guarantee that message will be shown with any log level settings
     */
    public void jenkins() {
        LoggerFactory.getLogger(this.getClass()).error(message);
    }

    public void everywhere() {
        testNG();
        allure();
        jenkins();
    }

}
