package Implementation;

import Configs.TestConfigsForAPIPetStore;
import Templates.SqlStatements;
import Utils.H2_DB_Manager;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GetPetServicesImplementation {

    PetStoreHelper objPetStoreHelper = new PetStoreHelper();
    Map<String, List<String>> findPetDataRetrievedFromExcel =null;
    Map<String, List<String>> findPetDataRecordFromDB=null;
    String petIDToFind=null;
    Response getPetByIdMethodResponse=null;
    SoftAssert sftAssert=null;
    String responseContentType =null;


    @But("Conditional_Environment_SetUp")
    public void Conditional_Environment_SetUp(){
        if(TestConfigsForAPIPetStore.ENV_FLAG.isEmpty()){
            TestConfigsForAPIPetStore.setConfigsForPetStore();
        }
    }// end Conditional_Environment_SetUp

    @BeforeStep
    public void setSftAssert(){
        sftAssert= new SoftAssert();
    }
    @AfterStep
    public void exeSoftAssert(){
        sftAssert.assertAll();
    }


    @Given("user_provides_pet_information\\({string},{string})")
    public void user_provides_pet_information(String uniqueTestID, String sheetName){
        findPetDataRetrievedFromExcel = objPetStoreHelper.getExcelPetDataToMap(uniqueTestID,sheetName);
    }

    @Given("user_calls_findPetByID_Service")
    public void user_calls_findPetByID_Service() {
        petIDToFind = findPetDataRetrievedFromExcel.get("Pet_id").get(0);//retrieve PetID from Excel
        //If PetID=USE_LATEST_FROM_DB, indicate to fetch latest H2 DB record matching TestCaseID
        // and use PetID of same to perform GET, this will insure we have valid data to enquire
        if(petIDToFind.equalsIgnoreCase("USE_LATEST_FROM_DB")){
            String filedName = "Test_Case_ID";
            String fieldValue = findPetDataRetrievedFromExcel.get("Test_Case_ID").get(0);
            String sqlSelectStatement = SqlStatements.selectLatestRecordFromPetDetails(filedName,fieldValue);
            findPetDataRecordFromDB = H2_DB_Manager.selectDBTableRecord(sqlSelectStatement);
            petIDToFind = findPetDataRecordFromDB.get("Pet_id").get(0);
            System.out.println("Pet ID from DB:"+petIDToFind);
        }
        getPetByIdMethodResponse = objPetStoreHelper.getPetWithPetIDMethod(findPetDataRetrievedFromExcel,petIDToFind);
        System.out.println("*******Get Pet By ID service response\n"+getPetByIdMethodResponse.asPrettyString());
    }

    @Then("getPet_service_returns_{int}")
    public void getPet_service_returns(int expStatusCode) {
        //fetch response header: Content-Type and perform schema validation accordingly
        responseContentType = getPetByIdMethodResponse.contentType();

        Assert.assertEquals(getPetByIdMethodResponse.getStatusCode(),expStatusCode,"Incorrect status code");
    }

    @Then("user_receives_pet_data_in_response_body")
    public void user_receives_pet_data_in_response_body() {
        String actualPetID=null;
        String actualPetName=null;

        //Add Schema validation and other checks based on response content type
        // As Path for XML and JSON will not be same due to additional root element(Pet) on XML
        ValidatableResponse validatableResponse = getPetByIdMethodResponse.then();
        if(responseContentType.equalsIgnoreCase("application/xml")) {
            validatableResponse.assertThat().body(RestAssuredMatchers.matchesXsd(new File("src/test/resources/PetSchema.xsd")));
            actualPetID=getPetByIdMethodResponse.path("Pet.id").toString();
            actualPetName=getPetByIdMethodResponse.path("Pet.name").toString();
        }else{
            validatableResponse.assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/PetSchema.json")));
            actualPetID=getPetByIdMethodResponse.path("id").toString();
            actualPetName=getPetByIdMethodResponse.path("name");
        }

       //Add assertions to check the fetched details are as expected
        sftAssert.assertEquals(actualPetID,petIDToFind,"Incorrect Pet ID");
        sftAssert.assertEquals(actualPetName, findPetDataRetrievedFromExcel.get("Pet_name").get(0),"Incorrect Pet Name");
        //Continue to add validation for all fields
    }

    @And("user_received_feedback_message_for_invalid_data\\({string},{string},{string})")
    public void user_received_feedback_message_for_invalid_data(String expCode, String expError, String expMessage) {
        String actCode= null;
        String actError= null;
        String actMessage= null;
        if(responseContentType.equalsIgnoreCase("application/xml")){
            actCode= getPetByIdMethodResponse.path("apiResponse.code").toString();
            actError= getPetByIdMethodResponse.path("apiResponse.type");
            actMessage= getPetByIdMethodResponse.path("apiResponse.message");

        }else{
            actCode= getPetByIdMethodResponse.path("code").toString();
            actError= getPetByIdMethodResponse.path("type");
            actMessage= getPetByIdMethodResponse.path("message");
        }

        sftAssert.assertEquals(actCode,expCode,"Code is incorrect");
        sftAssert.assertEquals(actError,expError,"Type is incorrect");
        sftAssert.assertEquals(actMessage,expMessage,"Message is incorrect");
    }

}//end class Implementation.AddPetServiceImplementation

