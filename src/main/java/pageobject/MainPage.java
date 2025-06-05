package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    public final static String MAIN_PAGE_URL = "https://qa-scooter.praktikum-services.ru/";

    private final WebDriver driver;
    // Вопрос
    private final String accordionQuestion = ".//div[@id = 'accordion__heading-%d']";
    // Ответ
    private final String accordionAnswer = ".//div[@id = 'accordion__panel-%d']//p";
    // Кнопка "Заказать" вверху страницы
    private final By orderHeaderButton = By.className("Button_Button__ra12g");


    // Кнопка "Заказать" в центре страницы
    private final By orderMiddleButton = By.xpath(".//*[contains(@class, 'Button_Button__ra12g') and contains(@class, 'Button_UltraBig__UU3Lp')]");
    // Раздел "Вопросы о важном"
    private final By sectionImportant = By.xpath(".//*[text()='Вопросы о важном']");
    // Кнопка согласия с cookies
    private final By cookiesButton = By.id("rcc-confirm-button");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public MainPage openMainPage() {
        driver.get(MAIN_PAGE_URL);
        return this;
    }

    public By getQuestions(int accordionQuestionIndex) {
        return By.xpath(String.format(accordionQuestion, accordionQuestionIndex));
    }

    public By getOrderMiddleButton() {
        return orderMiddleButton;
    }

    public By getAnswer(int accordionAnswerIndex) {
        return By.xpath(String.format(accordionAnswer, accordionAnswerIndex));
    }

    public By getSectionImportant() {
        return sectionImportant;
    }

    public void clickCookieesButton() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(cookiesButton))
                .click();
    }

    public void clickOrderHeaderButton() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(orderHeaderButton))
                .click();
    }

    public boolean checkOrderHeaderButtonIsEnabled() {
        return driver.findElement(orderHeaderButton).isEnabled();
    }

    public void clickOrderMiddleButton() {
        WebElement middleButton = driver.findElement(orderMiddleButton);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", middleButton);

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(middleButton))
                .click();
    }


    public boolean checkOrderMiddleButtonIsEnabled() {
        return driver.findElement(orderMiddleButton).isEnabled();
    }
}
