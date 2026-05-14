package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GooglePage {

    WebDriver driver;

    WebDriverWait wait;

    // Constructor
    public GooglePage(WebDriver driver) {

        this.driver = driver;

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Locators
    By searchBox = By.name("q");

    // Actions
    public void search(String text) {

        WebElement search =
                wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));

        search.sendKeys(text);

        search.sendKeys(Keys.ENTER);
    }

    // Validation
    public String getPageTitle() {

        return driver.getTitle();
    }
}