import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pageobject.MainPage;
import pageobject.OrderPage;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParametrazeOrderTest extends AbstractTest {
    private MainPage objMainPage;
    private OrderPage objOrderPage;


    @BeforeEach
    public void setUp() {
        baseSetUp();
        objMainPage = new MainPage(driver);
        objOrderPage = new OrderPage(driver);
        objMainPage.openMainPage();
        objMainPage.clickCookieesButton();
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
            objMainPage.clickOrderMiddleButton();
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

