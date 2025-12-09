package TestRunner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/features/",
        glue = {"Implementation","Configs","Utils"},
        tags = "@Sanity or @Regression or @Integration",
        monochrome = true,
        plugin = {"pretty", "html:target/cucumber-reports/html-report.html",
                  "json:target/cucumber-reports/json-report.json",
                  "junit:target/cucumber-reports/xml-report.xml"}
)
public class RunnerTestNG extends AbstractTestNGCucumberTests {

                /***********************************
                    public RunnerTestNG(){
                    System.setProperty("env","SIT");
                }
                ************************************/
}