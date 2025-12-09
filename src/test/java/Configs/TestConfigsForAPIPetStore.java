package Configs;
import Templates.SqlStatements;
import Utils.H2_DB_Manager;
import io.cucumber.java.BeforeAll;


public class TestConfigsForAPIPetStore {

    public static String BASE_URL = "";
    public static String ADD_PET_PATH = "";
    public static String ENV_FLAG = "";
    public static String GET_PET_PATH="";

    public static final String pathToExcelDataFile ="src/test/java/TestData/Data_PetStore.xlsx";
    public static final String pathAddPetRequestBodyXML = "/src/test/java/Templates/templateAddPetBodyXML.vm";
    public static final String pathAddPetRequestBodyJson = "/src/test/java/Templates/templateAddPetBodyJSON.vm";
    public static final String H2_DB_URL = "jdbc:h2:file:./testdb;AUTO_SERVER=TRUE";
    public static final String H2_DB_USER = "sa"; // default username
    public static final String H2_DB_PASS = "";   // default password is empty

    @BeforeAll
    public static void setConfigsForPetStore() {

        //Read environment from System Property, If not provided, use "SIT" by default.
        //Useful to switch between env while running from command line : mvn test -Denv=UAT -Dtest=RunnerTestNG
        String environment  = System.getProperty("env", "SIT");
        ENV_FLAG= environment;
        System.out.println("\n******** Env Flag value received:" + ENV_FLAG + " ********");
        switch(ENV_FLAG.toUpperCase()){

            case "SIT":
                BASE_URL = "https://petstore.swagger.io";
                ADD_PET_PATH = "/v2/pet";
                GET_PET_PATH = "/v2/pet/";
                System.out.println("As Instructed-Executing SIT environment");
                break;
            case "UAT":
                BASE_URL = "https://petstore.swagger.io";
                ADD_PET_PATH = "/v2/pet";
                GET_PET_PATH = "/v2/pet/";
                System.out.println("As Instructed-Executing UAT environment");
                break;
            case "DEV":
                BASE_URL = "https://petstore.swagger.io";
                ADD_PET_PATH = "/v2/pet";
                GET_PET_PATH = "/v2/pet/";
                System.out.println("As Instructed-Executing DEV environment");
                break;

            default:
                System.out.println("********** PLEASE PROVIDE VALID ENVIRONMENT- (DEV/SIT/UAT) **********");

        }//end switch-case

        //Create Tables conditionally
        H2_DB_Manager.createDBTable(SqlStatements.statementCreateTablePetDetails);

    }//end Configs.TestConfigsForAPIPetStore

}// end class
