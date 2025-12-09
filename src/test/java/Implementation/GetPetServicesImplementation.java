package Implementation;

import Configs.TestConfigsForAPIPetStore;
import Templates.SqlStatements;
import Utils.H2_DB_Manager;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

public class GetPetServicesImplementation {

    PetStore objPetStore = new PetStore();
    Map<String, List<String>> findPetDataReceived=null;
    Map<String, List<String>> findPetDataRecordFromDB=null;
    String petIDToFind=null;
    Response getPetByIdMethodResponse=null;
    SoftAssert sftAssert=null;


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
        findPetDataReceived = objPetStore.getExcelPetDataToMap(uniqueTestID,sheetName);
    }

    @Given("user_calls_findPetByID_Service")
    public void user_calls_findPetByID_Service() {
        petIDToFind = findPetDataReceived.get("Pet_id").get(0);//retrieve PetID from Excel
        //If PetID=False, indicate to fetch latest H2 DB record matching TestCaseID
        // and use PetID of same to perform GET, this will insure we have valid data to enquire
        if(petIDToFind.equalsIgnoreCase("FALSE")){
            String filedName = "Test_Case_ID";
            String fieldValue = findPetDataReceived.get("Test_Case_ID").get(0);
            String sqlSelectStatement = SqlStatements.selectLatestRecordFromPetDetails(filedName,fieldValue);
            findPetDataRecordFromDB = H2_DB_Manager.selectDBTableRecord(sqlSelectStatement);
            petIDToFind = findPetDataRecordFromDB.get("Pet_id").get(0);
            System.out.println("Pet ID from DB:"+petIDToFind);
        }
        getPetByIdMethodResponse =objPetStore.getPetWithPetIDMethod(petIDToFind);
        System.out.println("*******Get Pet By ID service response\n"+getPetByIdMethodResponse.asPrettyString());
    }

    @Then("getPet_service_returns_{int}")
    public void getPet_service_returns(int expStatusCode) {
        Assert.assertEquals(getPetByIdMethodResponse.getStatusCode(),expStatusCode,"Incorrect status code");
    }

    @Then("user_receives_pet_data_in_response_body")
    public void user_receives_pet_data_in_response_body() {
        //Add assertions to check the fetched details are as expected
        String actualPetID=getPetByIdMethodResponse.path("id").toString();
        String actualPetName=getPetByIdMethodResponse.path("name");

        sftAssert.assertEquals(actualPetID,petIDToFind,"Incorrect Pet ID");
        sftAssert.assertEquals(actualPetName,findPetDataReceived.get("Pet_name").get(0),"Incorrect Pet Name");
        //Continue to add validation for all fields
    }

    @And("user_received_feedback_message_for_invalid_data\\({string},{string},{string})")
    public void user_received_feedback_message_for_invalid_data(String expCode, String expError, String expMessage) {
        String actCode= getPetByIdMethodResponse.path("code").toString();
        String actError= getPetByIdMethodResponse.path("type");
        String actMessage= getPetByIdMethodResponse.path("message");
        sftAssert.assertEquals(actCode,expCode);
        sftAssert.assertEquals(actError,expError);
        sftAssert.assertEquals(actMessage,expMessage);
    }

}//end class Implementation.AddPetServiceImplementation

