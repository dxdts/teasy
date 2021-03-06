package com.wiley.autotest.selenium.context;

import com.wiley.autotest.ElementFinder;
import com.wiley.autotest.WebDriverAwareElementFinder;
import com.wiley.autotest.actions.Actions;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.*;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyWindow;
import com.wiley.autotest.selenium.elements.upgrade.Window;
import com.wiley.autotest.selenium.elements.upgrade.v3.CustomWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.v3.ElementWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurElementFinder;
import com.wiley.autotest.selenium.elements.upgrade.v3.Should;
import com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window.WindowMatcher;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;
import static com.wiley.autotest.selenium.elements.upgrade.CustomWebElementFactory.wrap;
import static com.wiley.autotest.utils.ExecutionUtils.isChrome;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 15.02.12
 * Time: 13:02
 */
public abstract class TeasyElementProvider {

    public static final Logger LOGGER = LoggerFactory.getLogger(TeasyElementProvider.class);

    @Autowired
    private long timeout;

    //default finder that uses timeout from pom
    private OurElementFinder finder;

    //VE added this to avoid No buffer space available exception. To be replaced with default value of 500 if does not work.
    protected static final long SLEEP_IN_MILLISECONDS = 1000;

    private OurElementFinder customFinder(SearchStrategy strategy) {
        return new OurElementFinder(getWebDriver(), strategy);
    }

    private OurElementFinder finder() {
        if (finder == null) {
            finder = new OurElementFinder(getWebDriver(), new SearchStrategy(timeout));
        }
        return finder;
    }

    protected CustomWaitFor waitFor() {
        return new CustomWaitFor();
    }

    protected CustomWaitFor waitFor(SearchStrategy strategy) {
        return new CustomWaitFor();
    }

    protected TeasyElement element(final By locator) {
        return finder().visibleElement(locator);
    }

    protected TeasyElement element(final By locator, SearchStrategy strategy) {
        return customFinder(strategy).visibleElement(locator);
    }

    protected List<TeasyElement> elements(final By locator) {
        return finder().visibleElements(locator);
    }

    protected List<TeasyElement> elements(final By locator, SearchStrategy strategy) {
        return customFinder(strategy).visibleElements(locator);
    }

    protected TeasyElement domElement(By locator) {
        return finder().presentInDomElement(locator);
    }

    protected TeasyElement domElement(By locator, SearchStrategy strategy) {
        return customFinder(strategy).presentInDomElement(locator);
    }

    protected List<TeasyElement> domElements(By locator) {
        return finder().presentInDomElements(locator);
    }

    protected List<TeasyElement> domElements(By locator, SearchStrategy strategy) {
        return customFinder(strategy).presentInDomElements(locator);
    }

    protected Alert alert() {
        return finder().alert();
    }

    protected Alert alert(SearchStrategy strategy) {
        return customFinder(strategy).alert();
    }

    protected Window window() {
        return new TeasyWindow(SeleniumHolder.getWebDriver());
    }

    protected Button button(By locator) {
        return getElement(Button.class, locator);
    }

    protected Button button(TeasyElement searchContext, By locator) {
        return getElement(Button.class, searchContext, locator);
    }

    protected List<Button> buttons(By locator) {
        return getElements(Button.class, locator);
    }

    protected List<Button> buttons(TeasyElement searchContext, By locator) {
        return getElements(Button.class, searchContext, locator);
    }

    protected Link link(By locator) {
        return getElement(Link.class, locator);
    }

    protected Link link(TeasyElement searchContext, By locator) {
        return getElement(Link.class, searchContext, locator);
    }

    protected List<Link> links(By locator) {
        return getElements(Link.class, locator);
    }

    protected List<Link> links(TeasyElement searchContext, By locator) {
        return getElements(Link.class, searchContext, locator);
    }

    protected CheckBox checkBox(By locator) {
        return getElement(CheckBox.class, locator);
    }

    protected CheckBox checkBox(TeasyElement searchContext, By locator) {
        return getElement(CheckBox.class, searchContext, locator);
    }

    protected List<CheckBox> checkBoxes(By locator) {
        return getElements(CheckBox.class, locator);
    }

    protected List<CheckBox> checkBoxes(TeasyElement searchContext, By locator) {
        return getElements(CheckBox.class, searchContext, locator);
    }

    protected RadioButton radioButton(By locator) {
        return getElement(RadioButton.class, locator);
    }

    protected RadioButton radioButton(TeasyElement searchContext, By locator) {
        return getElement(RadioButton.class, searchContext, locator);
    }

    protected List<RadioButton> radioButtons(By locator) {
        return getElements(RadioButton.class, locator);
    }

    protected List<RadioButton> radioButtons(TeasyElement searchContext, By locator) {
        return getElements(RadioButton.class, locator);
    }

    protected Select select(By locator) {
        return getElement(Select.class, locator);
    }

    protected Select select(TeasyElement searchContext, By locator) {
        return getElement(Select.class, searchContext, locator);
    }

    protected List<Select> selects(By locator) {
        return getElements(Select.class, locator);
    }

    protected List<Select> selects(TeasyElement searchContext, By locator) {
        return getElements(Select.class, locator);
    }

    protected TextField textField(By locator) {
        return getElement(TextField.class, locator);
    }

    protected TextField textField(TeasyElement searchContext, By locator) {
        return getElement(TextField.class, searchContext, locator);
    }

    protected List<TextField> textFields(By locator) {
        return getElements(TextField.class, locator);
    }

    protected List<TextField> textFields(TeasyElement searchContext, By locator) {
        return getElements(TextField.class, searchContext, locator);
    }

    private <T extends Element> T getElement(Class<T> elementType, By by) {
        return getWebElementWrapper(element(by)).getElement(elementType, by);
    }

    private <T extends Element> T getElement(Class<T> elementType, TeasyElement searchContext, By by) {
        return getWebElementWrapper(searchContext.element(by)).getElement(elementType, by);
    }

    private <T extends Element> List<T> getElements(Class<T> elementType, By by) {
        List<T> result = new ArrayList<>();
        elements(by)
                .forEach(element -> result.add(getWebElementWrapper(element).getElement(elementType, by)));
        return result;
    }

    private <T extends Element> List<T> getElements(Class<T> elementType, TeasyElement searchContext, By by) {
        List<T> result = new ArrayList<>();
        searchContext.elements(by)
                .forEach(element -> result.add(getWebElementWrapper(element).getElement(elementType, by)));
        return result;
    }

    public void init(final WebDriver driver, Long timeout) {
        elementFinder = new WebDriverAwareElementFinder(driver, new WebDriverWait(driver, timeout, SLEEP_IN_MILLISECONDS));
    }


    // OLD code that is going to be removed by September 2017.
    // Currently kept to give users some time to switch to new implementation

    @Deprecated
    /**
     * method will be deleted shortly. use elements() and check like isEmpty()
     */
    protected final TeasyElement elementOrNull(final By locator) {
        return elementOrNull(locator, new SearchStrategy(1).nullOnFailure());
    }

    @Deprecated
    /**
     * method will be deleted shortly. use elements(by, strategy) and check like isEmpty()
     */
    protected final TeasyElement elementOrNull(final By locator, SearchStrategy strategy) {
        List<TeasyElement> teasyElements = customFinder(strategy.nullOnFailure()).visibleElements(locator);
        if (teasyElements.isEmpty()) {
            return null;
        } else {
            return teasyElements.get(0);
        }
    }

    @Deprecated
    //will be replaced with OurElementFinder
    protected ElementFinder elementFinder;

    /**
     * Takes the 4th method from a StackTrace chain. It should be the method from Page that called method
     * Split method by words based on camel case naming convention
     * Make first letter of first word as upper case and all other words as lower case
     * for example for "generateErrorMessage" method should return "Generate error message"
     *
     * @return String of words separated by spaces
     * <p>
     * <p>
     * use {@link com.wiley.autotest.generator.ErrorMessage}
     */
    @Deprecated
    protected String generateErrorMessage() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length < 5) {
            return "***Code issue during generating error message for assert! Check the code at TeasyElementProvider";
        }

        String nameOfMethodThatCalledMe = "";
        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            StackTraceElement nextStackTraceElement;
            if (i + 1 >= stackTrace.length) {
                break;
            }
            nextStackTraceElement = stackTrace[i + 1];
            String abstractPageElement = "AbstractPageElement";
            String abstractElementFinder = "TeasyElementProvider";
            if ((stackTraceElement.getClassName().endsWith(abstractPageElement)
                    && !nextStackTraceElement.getClassName().endsWith(abstractPageElement)
                    && !nextStackTraceElement.getClassName().endsWith(abstractElementFinder)) ||
                    (stackTraceElement.getClassName().endsWith(abstractElementFinder)
                            && !nextStackTraceElement.getClassName().endsWith(abstractPageElement)
                            && !nextStackTraceElement.getClassName().endsWith(abstractElementFinder))) {
                nameOfMethodThatCalledMe = nextStackTraceElement.getMethodName();
                break;
            }
        }

        if (nameOfMethodThatCalledMe.isEmpty()) {
            nameOfMethodThatCalledMe = stackTrace[4].getMethodName();
        }

        String[] splitName = nameOfMethodThatCalledMe.split("(?<=[a-z])(?=[A-Z])");

        StringBuilder errorMessage = new StringBuilder();
        //make first letter of first word upper cased
        errorMessage.append(Character.toUpperCase(splitName[0].charAt(0))).append(splitName[0].substring(1));
        for (int i = 1; i < splitName.length; i++) {
            //make first letter of each of next words lower cased
            errorMessage.append(" ")
                    .append(Character.toLowerCase(splitName[i].charAt(0)))
                    .append(splitName[i].substring(1));
        }

        return errorMessage.toString() + " failed";
    }

    @Deprecated
    /**
     * Use {@link Window#close()}
     */
    protected final void closeBrowserWindow() {
        elementFinder.closeCurrentBrowserWindow();
    }

    @Deprecated
    /**
     * Use {@link Window#waitForScriptsToLoad()}
     */
    protected final void waitForPageToLoad() {
        elementFinder.waitForPageToLoad();
    }

    @Deprecated
    /**
     * Use {@link Window#switchToLast()} ()}
     */
    protected void switchToLastWindow() {
        elementFinder.switchToLastWindow();
    }

    @Deprecated
    //Outdated method - used only inside deprected methods. Will be removed in future.
    private Object getSupplierObject(Supplier webElementSupplier, String loggerMessage, String errorMessage) {
        try {
            return webElementSupplier.get();
        } catch (TimeoutException time) {
            fail(generateErrorMessage());
        } catch (WebDriverException wde) {
            LOGGER.error(loggerMessage, wde);
            fail((errorMessage.isEmpty() ? generateErrorMessage() : errorMessage)
                    + "\nException: " + wde.getMessage()
                    + "\nStackTrace: " + Arrays.toString(wde.getStackTrace()));
        }
        return null;
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#window()} and call {@link TeasyWindow#switchTo(WindowMatcher)}
     */
    protected final void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url, long timeoutInSeconds) {
        if (isChrome()) {
            TestUtils.waitForSomeTime(3000, "Wait for window is appear in chrome");
        }
        elementFinder.waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(url, timeoutInSeconds);
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#window()} and call {@link TeasyWindow#switchTo(WindowMatcher)}
     */
    protected final void waitForWindowToBeAppearedAndSwitchToIt(final String title) {
        Actions action = () -> {
            if (isChrome()) {
                TestUtils.waitForSomeTime(3000, "Wait for window is appear in chrome");
            }
            elementFinder.waitForWindowToBeAppearedAndSwitchToIt(title);
        };
        try {
            action.execute();
        } catch (WebDriverException e) {
            fail("Unable to find window with '" + title + "'");
        }
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#window()} and call {@link TeasyWindow#switchTo(WindowMatcher)}
     */
    protected final void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url) {
        Actions action = () -> {
            if (isChrome()) {
                TestUtils.waitForSomeTime(3000, "Wait for window is appear in chrome");
            }
            elementFinder.waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(url);

        };
        try {
            action.execute();
        } catch (WebDriverException e) {
            fail("Unable to find window with '" + url + "'");
        }
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#window()} and call {@link TeasyWindow#switchTo(WindowMatcher)}
     */
    protected final void waitForWindowToBeAppearedByPartialTitleAndSwitchToIt(final String partialTitle) {
        Actions action = () -> {
            if (isChrome()) {
                TestUtils.waitForSomeTime(3000, "Wait for window is appear in chrome");
            }
            elementFinder.waitForWindowToBeAppearedByPartialTitleAndSwitchToIt(partialTitle);
        };
        try {
            action.execute();
        } catch (WebDriverException e) {
            fail("Unable to find window with '" + partialTitle + "'");
        }
    }

    @Deprecated
    /**
     * Use element().domElements() or domElement().domeElements()
     */
    protected List<WebElement> domElements(SearchContext searchContext, By locator) {
        Supplier<List<WebElement>> webElementsSupplier = () -> waitForPresenceOfAllElementsLocatedBy(searchContext, locator);
        return (List<WebElement>) getSupplierObject(webElementsSupplier, "****WebDriverException in domElements()****", "");
    }

    @Deprecated
    /**
     * Use element().elementOrNull()
     */
    protected final WebElement elementOrNull(final SearchContext searchContext, final By locator) {

        throw new RuntimeException("REPLACE WITH NEW APPROACH IMMEDIATELY! METHOD IMPLEMENTATION WAS DELETED! CONTACT Vladimir Efimov vefimov@wiley.com");
//        try {
//            return elementFinder.waitForVisibilityOfAllElementsLocatedBy(searchContext, locator, (long) MIN_TIME_OUT_FOR_WAIT_IN_SECONDS)
//                    .get(0);
//        } catch (WebDriverException ignored) {
//            return null;
//        }
    }

    @Deprecated
    /**
     * Use element().domElement() or domElement().domeElement()
     */
    protected WebElement domElement(SearchContext searchContext, By locator) {
        Supplier webElementSupplier = () -> waitForPresenceOfElementLocatedBy(searchContext, locator);
        return (WebElement) getSupplierObject(webElementSupplier, "****WebDriverException in domElement()****", "");
    }

    @Deprecated
    /**
     * use element method with {@link SearchStrategy}
     * @param timeOutInSeconds
     */
    protected final void setTimeout(final long timeOutInSeconds) {
        elementFinder.setTimeout(timeOutInSeconds);
    }

    @Deprecated
    /**
     * use element().element() methods call chain
     */
    protected WebElement element(final SearchContext searchContext, final By locator) {
        Supplier<WebElement> webElementSupplier = () -> elementFinder.waitForVisibilityOfAllElementsLocatedBy(searchContext, locator)
                .get(0);
        return (WebElement) getSupplierObject(webElementSupplier, "****WebDriverException in element()****", "");
    }

    @Deprecated
    /**
     * use element().elements() methods call chain
     */
    protected List<WebElement> elements(final SearchContext searchContext, final By locator) {
        Supplier<List<WebElement>> webElementsSupplier = () -> elementFinder.waitForVisibilityOfAllElementsLocatedBy(searchContext, locator);
        return (List<WebElement>) getSupplierObject(webElementsSupplier, "****WebDriverException in elements()****", "");
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#domElement(By, SearchStrategy)}
     */
    protected WebElement domElement(By locator, final long timeoutInSeconds) {
        Supplier webElementSupplier = () -> waitForPresenceOfElementLocatedBy(locator, timeoutInSeconds);
        return (WebElement) getSupplierObject(webElementSupplier, "****WebDriverException in domElement()****", "");
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#element(By, SearchStrategy)}
     */
    protected final WebElement elementOrNull(final By locator, long timeoutInSeconds) {
        try {
            return elementFinder.waitForVisibilityOfAllElementsLocatedBy(locator, timeoutInSeconds).get(0);
        } catch (WebDriverException ignored) {
            return null;
        }
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#domElements(By, SearchStrategy)}
     */
    protected List<WebElement> domElements(By locator, long timeoutInSeconds) {
        return domElements(locator, OldSearchStrategy.FIRST_ELEMENTS, timeoutInSeconds);
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#domElements(By, SearchStrategy)}
     */
    protected List<WebElement> domElements(By locator, OldSearchStrategy oldSearchStrategy, long timeoutInSeconds) {
        Supplier<List<WebElement>> webElementsSupplier = () -> {
            switch (oldSearchStrategy) {
                case FIRST_ELEMENTS:
                    return waitForPresenceOfAllElementsLocatedBy(locator, timeoutInSeconds);
                case IN_ALL_FRAMES:
                    return elementFinder.waitForPresenceOfAllElementsLocatedByInFrames(locator, timeoutInSeconds);
            }
            throw new EnumConstantNotPresentException(oldSearchStrategy.getDeclaringClass(), " enum constant is not recognized");
        };
        return (List<WebElement>) getSupplierObject(webElementsSupplier, "****WebDriverException in domElements()****", "");
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#domElements(By)}
     */
    protected List<WebElement> domElements(By locator, OldSearchStrategy oldSearchStrategy) {
        Supplier<List<WebElement>> webElementsSupplier = () -> {
            switch (oldSearchStrategy) {
                case FIRST_ELEMENTS:
                    return waitForPresenceOfAllElementsLocatedBy(locator);
                case IN_ALL_FRAMES:
                    return elementFinder.waitForPresenceOfAllElementsLocatedByInFrames(locator);
            }
            throw new EnumConstantNotPresentException(oldSearchStrategy.getDeclaringClass(), " enum constant is not recognized");
        };
        return (List<WebElement>) getSupplierObject(webElementsSupplier, "****WebDriverException in domElements()****", "");
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#domElement(By, OldSearchStrategy)}
     */
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, long timeOutInSeconds) {
        return elementFinder.waitForPresenceOfElementLocatedBy(locator, timeOutInSeconds);
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#domElement(By)}
     */
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, final String errorMessage) {
        Supplier<WebElement> webElementSupplier = () -> waitForPresenceOfElementLocatedBy(locator);
        return (WebElement) getSupplierObject(webElementSupplier, "", errorMessage);
    }

    @Deprecated
    /**
     * Use element/select/textField/checkBox - when you need VISIBLE element
     * Use domElement - when you need element which is PRESENT IN DOM
     */
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, long timeOutInSeconds, final String errorMessage) {
        Supplier<WebElement> webElementSupplier = () -> waitForPresenceOfElementLocatedBy(locator, timeOutInSeconds);
        return (WebElement) getSupplierObject(webElementSupplier, "", errorMessage);
    }


    @Deprecated
    protected List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator) {
        return elementFinder.waitForPresenceOfAllElementsLocatedBy(locator);
    }

    @Deprecated
    protected final List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator, long timeoutInSeconds) {
        try {
            return elementFinder.waitForPresenceOfAllElementsLocatedBy(locator, timeoutInSeconds);
        } catch (TimeoutException ignored) {
            return new ArrayList<>();
        }
    }


    /**
     * Use elements/selects/textFields/checkBoxes - when you need VISIBLE element
     * Use domElement - when you need element which is PRESENT IN DOM
     */
    @Deprecated
    protected final List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator, String errorMessage) {
        try {
            return waitForPresenceOfAllElementsLocatedBy(locator);
        } catch (TimeoutException e) {
            fail(errorMessage);
        }
        return null;
    }

    /**
     * Instead of this method create a 1 line private getter with meaningful name in Page
     * and call element(By locator) instead of this method. Error message will be automatically generated based on this.
     * This method will be removed by Jan 2017.
     */
    @Deprecated
    protected final WebElement waitForVisibilityOfElementLocatedBy(final By locator, final String errorMessage) {
        Supplier<WebElement> webElementSupplier = () -> elementFinder.waitForVisibilityOfElementLocatedBy(locator);
        return (WebElement) getSupplierObject(webElementSupplier, "", errorMessage);
    }

    @Deprecated
    protected final WebElement waitForVisibilityOfElementLocatedBy(final By locator, long timeOutInSeconds) {
        return elementFinder.waitForVisibilityOfElementLocatedBy(locator, timeOutInSeconds);
    }

    @Deprecated
    //TODO VE: should be removed from framework
    public void waitUntilOnlyOneWindowIsOpen(String errorMessage) {
        try {
            elementFinder.waitUntilOnlyOneWindowIsOpen();
        } catch (TimeoutException e) {
            fail(errorMessage);
        }
    }

    /**
     * use {@link #elementOrNull(By)}
     * this method will be removed
     */
    @Deprecated
    protected final WebElement findElementByNoThrow(final By locator) {
        try {
            return elementFinder.findElementBy(locator);
        } catch (WebDriverException e) {
            return null;
        }
    }

    /**
     * use {@link #elementOrNull(SearchContext, By)}
     * this method will be removed
     */
    @Deprecated
    protected final WebElement findElementByNoThrow(final SearchContext searchContext, final By locator) {
        try {
            return elementFinder.findElementBy(searchContext, locator);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * use {@link #elementOrNull(By, long)}
     * this method will be removed
     */
    @Deprecated
    protected final WebElement waitForElementByNoThrow(final By locator, long timeOutInSeconds) {
        try {
            return waitForPresenceOfElementLocatedBy(locator, timeOutInSeconds);
        } catch (WebDriverException e) {
            return null;
        }
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#element(By, SearchStrategy)}
     */
    protected WebElement element(final By locator, final long timeoutInSeconds) {
        Supplier<WebElement> webElementSupplier = () -> elementFinder.waitForVisibilityOfAllElementsLocatedBy(locator, timeoutInSeconds)
                .get(0);
        return (WebElement) getSupplierObject(webElementSupplier, "****WebDriverException in element()****", "");
    }

    @Deprecated
    /**
     * Use {@link Should#haveAttribute(String, String)}
     */
    protected final void waitForElementContainsAttribute(final WebElement element, final String attributeName) {
        elementFinder.waitForElementContainsAttribute(element, attributeName);
    }

    @Deprecated
    /**
     * Use {@link Should#haveAttribute(String, String)}
     */
    protected final void waitForElementContainsAttribute(final WebElement element, final String attributeName, long timeOutInSeconds) {
        elementFinder.waitForElementContainsAttribute(element, attributeName, timeOutInSeconds);
    }

    @Deprecated
    /**
     * Use {@link Should#notHaveAttribute(String)}}
     */
    protected final void waitForElementNotContainsAttribute(final WebElement element, final String attributeName) {
        elementFinder.waitForElementNotContainsAttribute(element, attributeName);
    }

    @Deprecated
    /**
     * Use {@link Should#notHaveAttribute(String)}}
     */
    protected final void waitForElementNotContainsAttribute(final WebElement element, final String attributeName, long timeOutInSeconds) {
        elementFinder.waitForElementNotContainsAttribute(element, attributeName, timeOutInSeconds);
    }

    @Deprecated
    /**
     * Use {@link TeasyElementProvider#elements(By, SearchStrategy)}
     */
    protected List<WebElement> elements(final By locator, OldSearchStrategy oldSearchStrategy, final long timeoutInSeconds) {
        Supplier<List<WebElement>> webElementsSupplier = () -> {
            switch (oldSearchStrategy) {
                case FIRST_ELEMENTS:
                    return elementFinder.waitForVisibilityOfAllElementsLocatedBy(locator, timeoutInSeconds);
                case IN_ALL_FRAMES:
                    return elementFinder.waitForVisibilityOfAllElementsLocatedByInFrames(locator, timeoutInSeconds);
            }
            throw new EnumConstantNotPresentException(oldSearchStrategy.getDeclaringClass(), " enum constant is not recognized");
        };
        return (List<WebElement>) getSupplierObject(webElementsSupplier, "****WebDriverException in elements()****", "");
    }


    @Deprecated
    /**
     * Use {@link TeasyElementProvider#elements(By, SearchStrategy)}
     */
    protected List<WebElement> elements(final By locator, OldSearchStrategy oldSearchStrategy) {
        Supplier<List<WebElement>> webElementsSupplier = () -> {
            switch (oldSearchStrategy) {
                case FIRST_ELEMENTS:
                    return elementFinder.waitForVisibilityOfAllElementsLocatedBy(locator);
                case IN_ALL_FRAMES:
                    return elementFinder.waitForVisibilityOfAllElementsLocatedByInFrames(locator);
            }
            throw new EnumConstantNotPresentException(oldSearchStrategy.getDeclaringClass(), " enum constant is not recognized");
        };
        return (List<WebElement>) getSupplierObject(webElementsSupplier, "****WebDriverException in elements()****", "");
    }


    @Deprecated
    /**
     * Use {@link TeasyElementProvider#elements(By, SearchStrategy)}
     */
    protected List<WebElement> elements(final By locator, final long timeoutInSeconds) {
        return elements(locator, OldSearchStrategy.FIRST_ELEMENTS, timeoutInSeconds);
    }

    @Deprecated
    // Implement in your project if you need it. This method will be deleted
    // TODO VE: remove from framework
    protected final String waitForNewPopUpWindow(Set<String> windowHandles) {
        return elementFinder.waitForNewPopUpWindow(windowHandles);
    }

    @Deprecated
    /**
     * use methods of {@link ElementWaitFor} with {@link SearchStrategy#nullOnFailure()}
     */
    protected boolean booleanCondition(Condition condition, ConditionParams params, By locator) {
        try {
            condition(condition, params, locator);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor}
     */
    /**
     * This method is to be used for different conditions you want to wait for to happen with element using LOCATOR
     *
     * @param condition - condition to happen with element
     *                  for Locator "STALE" condition does not make sense so throwing an exception in case someone mistakenly decide to use it
     * @param locator   - locator for element you would like condition to happen
     */
    protected void condition(Condition condition, By locator) {
        condition(condition, new ConditionParams(), locator);
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor}
     */
    /**
     * This method is to be used for different conditions you want to wait for to happen with ELEMENT
     *
     * @param condition - condition to happen with element
     *                  for element "PRESENT_IN_DOM" and "ABSENT_IN_DOM" condition does not make sense so throwing an exception in case someone mistakenly decide to use it
     * @param element   - element you would like condition to happen
     */
    protected void condition(Condition condition, WebElement element) {
        condition(condition, null, element);
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor}
     */
    protected void condition(Condition condition, ConditionParams params, WebElement element) {
        switch (condition) {
            case VISIBLE: {
                elementFinder.waitForVisibilityOf(element);
                break;
            }
            case NOT_VISIBLE: {
                elementFinder.waitForInvisibilityOfElement(element);
                break;
            }
            case PRESENT_IN_DOM: {
                throw new RuntimeException("PRESENT_IN_DOM condition is not applicable for Element.");
            }
            case ABSENT_IN_DOM: {
                throw new RuntimeException("ABSENT_IN_DOM condition is not applicable for Element. Use STALE instead.");
            }
            case STALE: {
                waitForStalenessOf(element);
                break;
            }
            case LOCATION_NOT_CHANGED: {
                elementFinder.waitForElementNotChangeXLocation(element);
                break;
            }
            case HAS_TEXT: {
                if (params.getText() != null) {
                    waitForTextToBePresentIn(element, params.getText());
                }
                break;
            }
        }
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor}
     */
    protected void condition(Condition condition, ConditionParams params, By locator) {
        switch (condition) {
            case VISIBLE: {
                waitForVisibilityOfElementLocatedBy(locator, params.getTimeout());
                break;
            }
            case NOT_VISIBLE: {
                throw new RuntimeException("Please use different implementation from ElementWaitFor");
            }
            case PRESENT_IN_DOM: {
                waitForPresenceOfElementLocatedBy(locator);
                break;
            }
            case ABSENT_IN_DOM: {
                elementFinder.waitForAbsenceOfElementLocatedBy(locator, params.getTimeout());
                break;
            }
            case STALE: {
                throw new RuntimeException("STALE condition is not applicable for By. It is only applicable for existing WebElement");
            }
        }
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor}
     */
    public enum Condition {
        VISIBLE,
        NOT_VISIBLE,
        PRESENT_IN_DOM,
        ABSENT_IN_DOM,
        STALE,
        HAS_TEXT,
        LOCATION_NOT_CHANGED
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor}
     */
    public class ConditionParams {
        String text;
        long timeout = 0;

        public ConditionParams setShortTimeout() {
            timeout = 3;
            return this;
        }

        public ConditionParams setMediumTimeout() {
            timeout = 10;
            return this;
        }

        public ConditionParams setCommonTimeout() {
            timeout = 30;
            return this;
        }

        public ConditionParams setLongTimeout() {
            timeout = 60;
            return this;
        }

        public String getText() {
            return text;
        }

        public long getTimeout() {
            return timeout;
        }

        public ConditionParams setTimeout(long timeoutInSec) {
            this.timeout = timeoutInSec;
            return this;
        }

        public ConditionParams setText(String text) {
            this.text = text;
            return this;
        }
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#condition(Function)}
     * example: element.waitFor().text("some text");
     */
    protected final void customCondition(final ExpectedCondition<Boolean> condition, long timeout) {
        elementFinder.waitForCondition(condition, timeout);
    }

    @Deprecated
    /**
     * use methods like button(), select(), textField() directly.
     * This method will be made private
     */
    protected WebElementWrapper getWebElementWrapper(final WebElement wrappedElement) {
        return new WebElementWrapper(wrappedElement);
    }

    @Deprecated
    /**
     * will be made privatemi
     */
    protected WebElementWrapper getWebElementWrapper(TeasyElement wrappedElement) {
        return new WebElementWrapper(wrappedElement);
    }

    @Deprecated
    /**
     * Separate click and switch in two operations
     * 1. click
     * 2. switch to window
     */
    protected final void clickButtonAndWaitForNewPopUpWindowAndSwitchToIt(WebElement webElement) {
        elementFinder.waitForNewPopUpWindowAndSwitchToIt(webElement);
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#text(String)}
     * example: element.waitFor().text("some text");
     */
    protected final boolean isTextPresentInElement(final By locator, final String text) {
        return elementFinder.isTextPresentInElement(locator, text);
    }


    @Deprecated
    /**
     * use {@link ElementWaitFor#notContainsAttributeValue(String, String)}
     */
    protected final void waitForElementNotContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        elementFinder.waitForElementNotContainsAttributeValue(element, attributeName, attributeValue);
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#clickable()}
     */
    protected final void waitForElementToBeClickable(final WebElement element, final String errorMessage) {
        try {
            elementFinder.waitForElementToBeClickable(element);
        } catch (WebDriverException e) {
            fail(errorMessage);
        }
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#attribute(String, String)}
     * example: element.waitFor().attribute("color", "#ffffff");
     */
    protected final void waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        elementFinder.waitForElementContainsAttributeValue(element, attributeName, attributeValue);
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#attribute(String, String)}
     */
    protected final void waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue, long timeout) {
        elementFinder.waitForElementContainsAttributeValue(element, attributeName, attributeValue, timeout);
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#stale()}
     */
    protected final Boolean waitForStalenessOf(final WebElement webElement) {
        try {
            return elementFinder.waitForStalenessOf(webElement);
        } catch (UnreachableBrowserException ignored) {
            return true;
        }
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#stale()}
     */
    protected final Boolean waitForStalenessOf(final WebElement webElement, long timeout) {
        try {
            return elementFinder.waitForStalenessOf(webElement, timeout);
        } catch (UnreachableBrowserException ignored) {
            return true;
        }
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#stale()}
     */
    protected final Boolean waitForStalenessOf(final WebElement webElement, String errorMessage) {
        try {
            return waitForStalenessOf(webElement);
        } catch (TimeoutException e) {
            fail(errorMessage);
        }
        return false;
    }

    @Deprecated
    /**
     * use {@link com.wiley.autotest.utils.TestUtils#fail(String)} ()}
     */
    public static void fail(final String errorMessage) {
        Reporter.log("ERROR: " + errorMessage);
        throw new AssertionError(errorMessage);
    }

    @Deprecated
    /**
     * use this method copy from TestUtils
     */
    protected final void fail(final String errorMessage, Throwable cause) {
        Reporter.log("ERROR: " + errorMessage + cause.getMessage());
        throw new Error(errorMessage + cause.getMessage(), cause);
        //throw new AssertionError(errorMessage + cause.getMessage(), cause);//since 1.7
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#text(String)}
     */
    protected final Boolean waitForTextToBePresentIn(final WebElement element, final String text) {
        try {
            return elementFinder.waitForTextToBePresentIn(element, text);
        } catch (TimeoutException e) {
            fail("TimeoutException during waitForTextToBePresentIn(). Actual " + element.getText() + ". Expected text to be present " + text + ".");
        }
        return null;
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#text(String)}
     */
    protected final Boolean waitForTextToBePresentIn(final TextField textField, final String text) {
        return elementFinder.waitForTextToBePresentIn(textField, text);
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#text(String)}
     */
    protected final Boolean waitForTextToBePresentIn(final WebElement element) {
        return elementFinder.waitForTextToBePresentIn(element);
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#clickable()}
     */
    protected final WebElement waitForElementToBeClickable(final By locator, final String errorMessage) {
        try {
            return wrap(elementFinder.waitForElementToBeClickable(locator), locator);
        } catch (WebDriverException e) {
            fail(errorMessage);
        }
        return null;
    }

    @Deprecated
    /**
     * use {@link TeasyElementProvider#domElement(By)}
     */
    protected WebElement waitForPresenceOfElementLocatedBy(final By locator) {
        return elementFinder.waitForPresenceOfElementLocatedBy(locator);
    }

    @Deprecated
    /**
     * use {@link TeasyElementProvider#domElement(SearchContext, By)}
     */
    protected WebElement waitForPresenceOfElementLocatedBy(final SearchContext searchContext, final By locator) {
        return wrap(elementFinder.waitForPresenceOfElementLocatedBy(searchContext, locator), locator);
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#text(String)}
     */
    protected final Boolean waitForTextToBePresentInElement(final By locator, final String text) {
        return elementFinder.waitForTextToBePresentInElement(locator, text);
    }

    @Deprecated
    /**
     * use {@link ElementWaitFor#text(String)}
     */
    protected final Boolean waitForTextToBePresentInElement(final By locator) {
        return elementFinder.waitForTextToBePresentInElement(locator);
    }

    @Deprecated
    /**
     * use {@link TeasyElementProvider#domElements(SearchContext, By)}
     */
    protected List<WebElement> waitForPresenceOfAllElementsLocatedBy(final SearchContext searchContext, final By locator) {
        return elementFinder.waitForPresenceOfAllElementsLocatedBy(searchContext, locator);
    }

}

