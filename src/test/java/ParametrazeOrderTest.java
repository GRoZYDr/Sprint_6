
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobject.MainPage;
import pageobject.OrderPage;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParametrazeOrderTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private MainPage objMainPage;
    private OrderPage objOrderPage;


    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();//или WebDriverManager.firefoxdriver().setup();
        driver = new ChromeDriver();//или driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        objMainPage = new MainPage(driver);
        objOrderPage = new OrderPage(driver);
        objMainPage.openMainPage();
        objMainPage.clickCookieesButton();
    }


    @AfterEach
    void tearDown() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ignored) {
        }
        driver.quit();
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void testOrderWithDifferentData(
            String buttonLocation,
            String name,
            String surname,
            String address,
            String metro,
            String number,
            int date,
            String days,
            String comment) {

        if ("HEADER".equals(buttonLocation)) {
            objMainPage.clickOrderHeaderButton();
        } else if ("MIDDLE".equals(buttonLocation)) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement middleButton = driver.findElement(objMainPage.getOrderMiddleButton());
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", middleButton);
            middleButton.click();
        }

        //Нажать кнопку "Заказать"
        objMainPage.clickOrderHeaderButton();
        //Проверка перехода на страницу формы заполнения данных
        String title = driver.getTitle();
        assertEquals("undefined", title);
        //Заполнить форму данными
        objOrderPage.fillName(name);
        objOrderPage.fillSurname(surname);
        objOrderPage.fillAdress(address);
        objOrderPage.fillMetro();
        objOrderPage.fillTelephone(number);
        //Переход на следующую страницу, проверка перехода
        objOrderPage.clickButtonNext();
        String titleOrder = driver.getTitle();
        assertEquals("undefined", titleOrder);
        //Оформление заказа
        objOrderPage.fillDeliveryDateWithCurrentDatePlusDays(date); //Выбор даты заказа
        objOrderPage.selectOption(days);
        objOrderPage.сhoiceColorScooter();
        objOrderPage.writeComment(comment);
        objOrderPage.clickButtonOrder();
        //Нажать "Да" для согласия и проверка сообщения об успешном заказе
        objOrderPage.clickButtonYes();
        assertTrue(objOrderPage.isOrderSuccessDisplayed());

    }

    static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of("HEADER", "Симба", "Лев", "Саванна",
                        "Медведково", "88005553535", 1, "трое суток", "Лучше позвонить"),
                Arguments.of("MIDDLE", "Пумба", "Бородавочник", "Саванна",
                        "Бабушкинская", "+78005553535", 2, "сутки", "Чем занимать")
        );
    }
}

