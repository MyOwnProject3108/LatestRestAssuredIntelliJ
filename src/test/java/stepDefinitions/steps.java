package stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
//import cucumber.api.DataTable;
import io.restassured.response.Response;
import org.junit.Assert;
import pages.TabRequestCalls;
import utils.TabUtils;
import utils.UpdateTabUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

//import io.cucumber.datatable.DataTable;


public class steps  {


    public static Properties config;
    String baseURl;
    public String id;
    TabRequestCalls sendCall;
    public Response response;
    TabUtils testUtils = new TabUtils();
    UpdateTabUtils updateTabUtils = new UpdateTabUtils();




    public steps() throws IOException {
        config = new Properties();
        final String appConfigPath = System.getProperty("appconfig") != null ? System.getProperty("appconfig") : "\\src\\test\\java\\Config\\config.properties";
        FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + appConfigPath);
        config.load(ip);
        baseURl = config.getProperty("baseURL");
    }

    @Given("^I logged in as \"(.*?)\"$")
    public void i_logged_in_as(String xUserId) throws Throwable {

        if (xUserId.equalsIgnoreCase("Broadcast Manager")) {
            id = config.getProperty("btm_hub_xUserId");

        }

        else if (xUserId.equalsIgnoreCase("Traffic Manager")) {
            id = config.getProperty("ttm_xUserId");

        }

        else if (xUserId.equalsIgnoreCase("UnAuth User")){
            id = config.getProperty("unauth_userId");
        }

        if (xUserId.equalsIgnoreCase("BTMUnAuth User")) {
            id = config.getProperty("btm_UnAuthId");
        }

        else if (xUserId.equalsIgnoreCase("BTMNullAuthUser")) {
            id = config.getProperty("btm_NullAuth_userId");
        }

    }

    @When("I retrieve tabs for the user \"(.*?)\"$")
    public void i_retrieve_tabs_for_the_user(String xUserId)throws Throwable {

        sendCall = new TabRequestCalls(baseURl);

        if (xUserId.equalsIgnoreCase("Traffic Manager")) {
            id = config.getProperty("ttm_xUserId");

        } else if (xUserId.equalsIgnoreCase("Broadcast Manager")) {
            id = config.getProperty("btm_hub_xUserId");

        } else if (xUserId.equalsIgnoreCase("UnAuth User")) {
            id = config.getProperty("unauth_userId");
        } else if (xUserId.equalsIgnoreCase("BTMUnAuth User")) {
            id = config.getProperty("btm_UnAuthId");
        }

        if (xUserId.equalsIgnoreCase("BTMNullAuthUser") || xUserId.equalsIgnoreCase("NullAuthUser")) {
            response = sendCall.getTabUnAuth(config.getProperty("gettabendpoint"));
        } else {
            response = sendCall.getCall(config.getProperty("gettabendpoint"), id);
        }
        System.out.println(response.asString());

    }



    @When("^I create a tab for the user \"(.*?)\" with the following payload$")
    public void i_create_a_tab_for_the_user_with_the_following_payload(String xUserId, DataTable dataTable1) throws Throwable {

        List<List<String>> data = dataTable1.asLists();

        testUtils.setName(data.get(1).get(0));
        testUtils.setPublic(Boolean.parseBoolean(data.get(1).get(1)));
        testUtils.setDefault(Boolean.parseBoolean(data.get(1).get(2)));
        testUtils.setTabType(data.get(1).get(3));

        sendCall = new TabRequestCalls(baseURl);
        String endpoint = config.getProperty("posttab");
        System.out.println("printing end point..." + endpoint);

        if (xUserId.equalsIgnoreCase("Traffic Manager")) {
            xUserId = config.getProperty("ttm_xUserId");
        } else if (xUserId.equalsIgnoreCase("Broadcast Manager")) {

            xUserId = config.getProperty("btm_hub_xUserId");

        } else if (xUserId.equalsIgnoreCase("UnAuth User")) {

            xUserId = config.getProperty("unauth_userId");

        } else if (xUserId.equalsIgnoreCase("BTMUnAuth User")) {
            xUserId = config.getProperty("btm_UnAuthId");
        }


        if (xUserId.equalsIgnoreCase("NullAuthUser") || xUserId.equalsIgnoreCase("BTMNullAuthUser")) {
            xUserId = config.getProperty("nullauth_userId");

            response = sendCall.postCallWithUnAuthUserId(testUtils, endpoint);
            //   response.then().log().all().statusCode(400).assertThat().body(matchesJsonSchemaInClasspath(""));
            System.out.println("Printing response........." + response.asString());
        } else {
            response = sendCall.postCallWithUserId(testUtils, endpoint, xUserId);
            System.out.println("Printing response........." + response.asString());

        }

    }

    @Then("^I should see response code with \"(.*?)\"$")
    public void i_should_see_response_code_with(String statusCode) throws Throwable {
        Assert.assertEquals(statusCode, Integer.toString(response.getStatusCode()));
    }


    @Then("^I should see valid response code \"(.*?)\" with \"(.*?)\"$")
    public void i_should_see_valid_response_code_with(String responseCode, String responseMessage) throws Throwable {
        if (responseCode.equalsIgnoreCase("200")) {


            Assert.assertEquals(responseCode, Integer.toString(response.getStatusCode()));
            Assert.assertNotNull(response.path("_id"));

        }
    }


    @Then("^I should see below response code \"(.*?)\" with \"(.*?)\"$")
    public void i_should_see_below_response_code_with(String responseCode, String responseMessage) throws Throwable {
        if (responseCode.equalsIgnoreCase("403")) {

            Assert.assertEquals(responseCode, Integer.toString(response.getStatusCode()));
            Assert.assertEquals(responseMessage, response.asString());


        } else if (responseCode.equalsIgnoreCase("400")) {

            Assert.assertEquals(responseCode, Integer.toString(response.getStatusCode()));
            Assert.assertEquals(responseMessage, response.asString());

        }

    }


    @Given("^I update tab for the user \"(.*?)\" with the following payload$")
    public void i_update_tab_for_the_user_with_the_following_payload(String xUserId, DataTable arg2) throws Throwable {

        List<List<String>> data = arg2.asLists();
        String tabId = response.path("_id");
        System.out.println("printing response update method...." + tabId);
        updateTabUtils.set_id(tabId);

        String createByUserId = response.path("createByUserId");
        updateTabUtils.setCreateByUserId(createByUserId);

        updateTabUtils.setName(data.get(1).get(0));
        updateTabUtils.setPublic(Boolean.parseBoolean(data.get(1).get(1)));
        updateTabUtils.setDefault(Boolean.parseBoolean(data.get(1).get(2)));
        updateTabUtils.setTabType(data.get(1).get(3));

        sendCall = new TabRequestCalls(baseURl);
        String endpoint = config.getProperty("updatetabendpoint");

        if (xUserId.equalsIgnoreCase("Broadcast Manager")) {
            xUserId = config.getProperty("btm_hub_xUserId");
        } else if (xUserId.equalsIgnoreCase("Traffic Manager")) {
            xUserId = config.getProperty("ttm_xUserId");
        } else if (xUserId.equalsIgnoreCase("UnAuth User")) {
            xUserId = config.getProperty("unauth_userId");
        } else if (xUserId.equalsIgnoreCase("BTMUnAuth User")) {
            xUserId = config.getProperty("btm_UnAuthId");
        }

        if (xUserId.equalsIgnoreCase("NullAuthUser") || (xUserId.equalsIgnoreCase("BTMNullAuthUser"))) {
            xUserId = config.getProperty("nullauth_userId");


            response = sendCall.putCallWithInValidUserId(updateTabUtils, endpoint);
            System.out.println("printing response...." + response.asString());
        } else {

            response = sendCall.putCallWithValidUserId(updateTabUtils, endpoint, xUserId);
            System.out.println("printing response........." + response.asString());

        }
    }

    @Given("^I update order of tab for the user \"(.*?)\"$")
    public void i_update_order_of_tab_for_the_user(String xUserId) throws Throwable {
        String tabIds = response.path("_id");

        if (xUserId.equalsIgnoreCase("NullAuthUser") || (xUserId.equalsIgnoreCase("BTMNullAuthUser"))) {
            response = sendCall.updateTabOrder(tabIds, config.getProperty("updatetaborderendpoint"), config.getProperty("nullauth_userId"));
            System.out.println("printing json response..." + response.asString());
        }
    }







    @Then("^I should see following details in Retrieve tabs response payload$")
    public void i_should_see_following_details_in_Retrieve_response_payload(DataTable arg1) throws Throwable {

        List<List<String>> data = arg1.asLists();
     //   Assert.assertEquals(data.get(1).get(0), response.path("_id"));
        Assert.assertEquals(data.get(1).get(0), response.path("name"));
        Assert.assertEquals(data.get(1).get(1), response.path("businessUnitId"));
    }




    @Given("^I delete tab for the user \"(.*?)\"$")
    public void i_delete_tab_for_the_user(String xUserId) throws Throwable {
        String tabId = response.path("_id");
        sendCall = new TabRequestCalls(baseURl);

        if (xUserId.equalsIgnoreCase("Traffic Manager")) {
            response = sendCall.deleteTab(config.getProperty("deleteendpoint") + tabId, config.getProperty("ttm_xUserId"));
        } else if (xUserId.equalsIgnoreCase("Broadcast Manager")) {
            response = sendCall.deleteTab(config.getProperty("deleteendpoint") + tabId, config.getProperty("btm_hub_xUserId"));
        } else if (xUserId.equalsIgnoreCase("UnAuth User")) {
            response = sendCall.deleteTab(config.getProperty("deleteendpoint") + tabId, config.getProperty("unauth_userId"));
        } else if (xUserId.equalsIgnoreCase("BTMUnAuth User")) {
            response = sendCall.deleteTab(config.getProperty("deleteendpoint") + tabId, config.getProperty("btm_UnAuthId"));
        }

        if (xUserId.equalsIgnoreCase("NullAuthUser") || xUserId.equalsIgnoreCase("BTMNullAuthUser")) {
            response = sendCall.deleteUnAuthTab(config.getProperty("gettabendpoint"));
        }
        System.out.println(response.asString());

    }


    @Then("^I should see response code \"(.*?)\" with status \"(.*?)\"$")
    public void i_should_see_response_code_with_status(String responseCode, String responseStatus) throws Throwable {
        if (responseCode.equalsIgnoreCase("200")) {
            Assert.assertEquals(responseCode, Integer.toString(response.getStatusCode()));
            Assert.assertEquals(responseStatus, response.asString());
        }
    }


}
