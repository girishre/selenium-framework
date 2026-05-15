package base;

import io.qameta.allure.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.GooglePage;
import java.time.Duration;

@Epic("Google Search")
@Feature("Search Functionality")
public class GoogleTest extends BaseTest {

    @Test(description = "Verify search results title contains keyword")
    @Story("User searches for Selenium")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Opens Google, searches for Selenium, and validates the page title")
    public void searchSelenium() {

        GooglePage googlePage = new GooglePage(driver);

        googlePage.search("Selenium");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains("Selenium"));

        String title = googlePage.getPageTitle();
        System.out.println("Page Title: " + title);

        Assert.assertTrue(title.contains("Selenium"));
        System.out.println("Validation Passed");
    }
}