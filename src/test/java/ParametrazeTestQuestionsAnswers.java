import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobject.MainPage;

import java.time.Duration;
import java.util.stream.Stream;

public class ParametrazeTestQuestionsAnswers {
    private WebDriver driver;
    private WebDriverWait wait;
    private MainPage objMainPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        objMainPage = new MainPage(driver);
        objMainPage.openMainPage();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void testQuestionsAnswer(int questionsIndex, String expectedAnswer) {
        // Нажать на кнопку согласия с cookies
        objMainPage.clickCookieesButton();

        // Скрол до раздела "Вопросы о важном"
        WebElement sectionImportant = driver.findElement(objMainPage.getSectionImportant());
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", sectionImportant);

        // Нажать на вопрос по индексу
        WebElement question = wait.until(ExpectedConditions.elementToBeClickable(objMainPage.getQuestions(questionsIndex)));
        question.click();

        // Получить текст ответа
        WebElement answer = wait.until(ExpectedConditions.visibilityOfElementLocated(objMainPage.getAnswer(questionsIndex)));
        String actualAnswer = answer.getText();

        // Сравнить с ожидаемым
        Assertions.assertEquals(expectedAnswer, actualAnswer, "Ответ не совпадает с ожидаемым.");
    }

    static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой."),
                Arguments.of(1, "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."),
                Arguments.of(2, "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."),
                Arguments.of(3, "Только начиная с завтрашнего дня. Но скоро станем расторопнее."),
                Arguments.of(4, "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."),
                Arguments.of(5, "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."),
                Arguments.of(6, "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."),
                Arguments.of(7, "Да, обязательно. Всем самокатов! И Москве, и Московской области.")
        );
    }
}
