package base;

import io.qameta.allure.Allure;
import listeners.ExtentReportListener;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

@Listeners(ExtentReportListener.class)
public class BaseTest {

    public WebDriver driver;
    public Properties prop;

    @BeforeMethod
    @Parameters("browser")
    public void setup(@Optional("") String browser) throws IOException, InterruptedException {

        prop = new Properties();
        FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "/src/test/resources/config/config.properties");
        prop.load(fis);

        if (browser.isEmpty()) {
            browser = prop.getProperty("browser");
        }

        boolean isCI = System.getenv("JENKINS_URL") != null;

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            if (isCI) {
                // Headless mode for Jenkins/Docker
                chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
            } else {
                // Normal mode for local
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36");
            }
            driver = new ChromeDriver(chromeOptions);

        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();

        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("general.useragent.override",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:124.0) Gecko/20100101 Firefox/124.0");
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setProfile(profile);
            driver = new FirefoxDriver(firefoxOptions);

        } else {
            throw new RuntimeException("Invalid browser name: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(Long.parseLong(prop.getProperty("timeout"))));
        driver.get(prop.getProperty("url"));
        Thread.sleep(2000);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            captureScreenshot(result.getMethod().getMethodName());
        }
        if (driver != null) {
            driver.quit();
        }
    }

    public void captureScreenshot(String testName) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(testName + "_screenshot",
                    "image/png",
                    new java.io.ByteArrayInputStream(screenshot),
                    "png");
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }
}