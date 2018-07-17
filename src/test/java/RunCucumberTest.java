
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.io.IOException;


@RunWith(Cucumber.class)
@CucumberOptions(features = "src\\test\\resources",glue = "stepDefinitions",tags= {"@ValidUserCreateTab" })
public class RunCucumberTest {

    @BeforeClass
    public static void setup() throws IOException {


    }

    @AfterClass
    public static void teardown(){

    }

}
