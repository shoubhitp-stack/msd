import java.io.File;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LocalLoginTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");          // Required for GitHub Actions Linux runner
        options.addArguments("--no-sandbox");            // Bypasses OS security model in CI containers
        options.addArguments("--disable-dev-shm-usage"); // Prevents /dev/shm memory crashes on Linux
        options.addArguments("--window-size=1920,1080"); // Replaces .maximize() for headless mode

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Verify this matches your folder name ("webpage/login.html" or "webage/login.html")
        String page = new File("webpage/login.html")
                .getAbsoluteFile()
                .toURI()
                .toString();

        driver.get(page);
    }

    @Test
    public void validLoginTest() {
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("12345");
        driver.findElement(By.id("loginButton")).click();

        String message = driver.findElement(By.id("message")).getText();
        System.out.println("Message: " + message);
        Assert.assertEquals(message, "Login Successful");
    }

    @Test
    public void invalidLoginTest() {
        driver.findElement(By.id("username")).sendKeys("wrong");
        driver.findElement(By.id("password")).sendKeys("wrong");
        driver.findElement(By.id("loginButton")).click();

        String message = driver.findElement(By.id("message")).getText();
        System.out.println("Message: " + message);
        Assert.assertEquals(message, "Invalid Username or Password");
    }

    @Test
    public void checkBackgroundColor() {
        String bgColor = driver.findElement(By.id("loginBox"))
                               .getCssValue("background-color");

        System.out.println("Background Color: " + bgColor);
        Assert.assertEquals(bgColor, "rgba(173, 216, 230, 1)");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}