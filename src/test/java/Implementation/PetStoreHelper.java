package Implementation;

import Configs.TestConfigsForAPIPetStore;
import Utils.DataFormatter;
import Utils.DateTimeUtils;
import Utils.ExcelDataManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.testng.Assert;

import java.io.StringWriter;
import java.util.*;

import static io.restassured.RestAssured.given;

public class PetStoreHelper {

    String[] dataSet = null;
    DataFormatter objDataFormatter = new DataFormatter();
    ExcelDataManager objExcelDataManager = new ExcelDataManager();

    public Map<String, List<String>> getExcelPetDataToMap(String uniqueTestID, String sheetName){
        dataSet = objExcelDataManager.getDataFromExcel(uniqueTestID,sheetName);
        /*for (int i=0;i<dataSet.length;i++){
            System.out.println("Data:"+dataSet[i]);
        }*/
        List<String> photoUrls = Arrays.asList(dataSet[10].split(";"));
        List<String> tagIds = Arrays.asList(dataSet[11].split(";"));
        List<String> tagNames = Arrays.asList(dataSet[12].split(";"));

        Map<String, List<String>> dataMap = new HashMap<>();
        dataMap.put("Test_Case_ID", Collections.singletonList(dataSet[0]));
        dataMap.put("HRD_ACCEPT_NAME", Collections.singletonList(dataSet[1]));
        dataMap.put("HRD_ACCEPT_VALUE", Collections.singletonList(dataSet[2]));
        dataMap.put("HRD_CONTENTTYPE_NAME", Collections.singletonList(dataSet[3]));
        dataMap.put("HRD_CONTENTTYPE_VALUE", Collections.singletonList(dataSet[4]));
        dataMap.put("PayloadType", Collections.singletonList(dataSet[5]));
        dataMap.put("Pet_id", Collections.singletonList(dataSet[6]));
        dataMap.put("Pet_Category_id", Collections.singletonList(dataSet[7]));
        dataMap.put("Pet_Category_name", Collections.singletonList(dataSet[8]));
        dataMap.put("Pet_name", Collections.singletonList(dataSet[9]));
        dataMap.put("Pet_photoUrls_photoUrl", photoUrls);
        dataMap.put("Pet_tags_Tag_id", tagIds );
        dataMap.put("Pet_tags_Tag_Name", tagNames);
        dataMap.put("Pet_status", Collections.singletonList(dataSet[13]));

        /*Set<String> keys = dataMap.keySet();
        System.out.println("Map:"+dataMap.values().toString());
        for (String key: keys){
            System.out.println("Value:"+dataMap.get(key));
        }*/

        return dataMap;
    }//end getExcelPetData

    public String generateAddPetRequestBodyFromMap(Map<String,List<String>> mapData) {

        String strBody="";

        //VelocityContext- implementation that allows for the storage and retrieval of data.
        VelocityContext vc = new VelocityContext();
        vc.put("PetBody",mapData);

        VelocityEngine ve = new VelocityEngine();
        ve.init();

        //Load the template
        String pathToTemplate="";
        String typeOfPayload = mapData.get("PayloadType").get(0).toUpperCase();
        System.out.println("Payload Type is:"+typeOfPayload);
        if(typeOfPayload.equalsIgnoreCase("XML")){
            pathToTemplate = TestConfigsForAPIPetStore.pathAddPetRequestBodyXML;
        }else if(typeOfPayload.equalsIgnoreCase("JSON")){
            pathToTemplate = TestConfigsForAPIPetStore.pathAddPetRequestBodyJson;
        }else{
            System.out.println("********* Please provide payload type either XML or JSON");
            Assert.assertTrue((typeOfPayload.contains("JSON") || typeOfPayload.contains("XML")),"Invalid payload type");
        }
        Template vt = ve.getTemplate(pathToTemplate);

        StringWriter writer = new StringWriter(); //write strings to an in-memory string buffer
        vt.merge(vc,writer);

        strBody = writer.toString();

        //System.out.println("********* Add Pet-Request Payload Generated:\n"+strBody);

        String formattedBody = null;
        try {
            if(typeOfPayload.equalsIgnoreCase("JSON")){
                formattedBody = objDataFormatter.formatJsonStringRemoveEmptyElements(strBody);
            }else{
                formattedBody = objDataFormatter.formatXMLStringRemoveEmptyTags(strBody);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("********* Add Pet-Request Payload Formatted:\n"+formattedBody);

        return formattedBody;
    }//adPetRequestBody

    public Response addPetMethod(Map<String,List<String>> addPetData, String requestBody){

        String addPetEndPointURL = TestConfigsForAPIPetStore.BASE_URL + TestConfigsForAPIPetStore.ADD_PET_PATH ;
        System.out.println("***** Hitting add pet URL:"+addPetEndPointURL);
        RequestSpecification reqSpec = new RequestSpecBuilder()
                .addHeader(addPetData.get("HRD_ACCEPT_NAME").get(0),addPetData.get("HRD_ACCEPT_VALUE").get(0))
                .addHeader(addPetData.get("HRD_CONTENTTYPE_NAME").get(0),addPetData.get("HRD_CONTENTTYPE_VALUE").get(0))
                .build();

        Response response=
                given()
                        .spec(reqSpec)
                        .body(requestBody)
                .when()
                        .post(addPetEndPointURL)
                .then()
                        .log().all()
                        .extract().response();

        //System.out.println("Response received from -Add Pet- service:"+response.prettyPrint());

        return response;

    }//end addPetEndPoint

    public Response getPetWithPetIDMethod(Map<String,List<String>> petData,String id){

        RequestSpecification reqSpec = new RequestSpecBuilder()
                .addHeader(petData.get("HRD_ACCEPT_NAME").get(0),petData.get("HRD_ACCEPT_VALUE").get(0))
                .build();

        String getPetEndPointURL = TestConfigsForAPIPetStore.BASE_URL + TestConfigsForAPIPetStore.GET_PET_PATH +"{petId}" ;
        System.out.println("****** Hitting Get Pet ByID:"+getPetEndPointURL);
        Response response=
                given()
                        .spec(reqSpec)
                        .pathParam("petId",id)
                .when()
                        .get(getPetEndPointURL)
                .then()
                        .log().all()
                        .extract().response();

        return response;
    }//end getPetData

    public Response getPetWithPetIDMethod(String id){

        String getPetEndPointURL = TestConfigsForAPIPetStore.BASE_URL + TestConfigsForAPIPetStore.GET_PET_PATH +"{petId}" ;
        System.out.println("****** Hitting Get Pet ByID:"+getPetEndPointURL);
        Response response=
                given()
                        .pathParam("petId",id)
                .when()
                        .get(getPetEndPointURL)
                .then()
                        .log().all()
                        .extract().response();

        return response;
    }//end getPetData

    public String[] getExcelPetData(String uniqueTestID,String sheetName){
        dataSet = objExcelDataManager.getDataFromExcel(uniqueTestID,sheetName);
        /*for (int i=0;i<dataSet.length;i++){
            System.out.println("Data:"+dataSet[i]);
        }*/

        return dataSet;
    }//end getExcelPetData

}// end class PetSore

