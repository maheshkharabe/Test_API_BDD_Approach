package Templates;

import java.util.List;
import java.util.Map;

public class SqlStatements {

    public static final String statementCreateTablePetDetails =
            "CREATE TABLE IF NOT EXISTS PET_DETAILS( \n" +
                    "id BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                    "Test_Case_ID VARCHAR(255),\n" +
                    "Pet_id VARCHAR(255),\n" +
                    "Pet_Category_id VARCHAR(255),\n" +
                    "Pet_Category_name VARCHAR(255), \n" +
                    "Pet_name VARCHAR(255),\n" +
                    "Pet_photoUrls_photoUrl VARCHAR(255),\n" +
                    "Pet_tags_Tag_id VARCHAR(255),\n" +
                    "Pet_tags_Tag_Name VARCHAR(255),\n" +
                    "Pet_status VARCHAR(255),\n" +
                    "PRIMARY KEY (id)\n"+
                    ")";

    public static String creteInsertStatementOnPetDetails(Map<String, List<String>> excelData){
        String testCaseID = excelData.get("Test_Case_ID").get(0);
        String petID = excelData.get("Pet_id").get(0);
        String petCatID = excelData.get("Pet_Category_id").get(0);
        String petCatName = excelData.get("Pet_Category_name").get(0);
        String petName = excelData.get("Pet_name").get(0);
        String petPhotoUrls = excelData.get("Pet_photoUrls_photoUrl").toString();
        String petTagID = excelData.get("Pet_tags_Tag_id").toString();
        String petTagName = excelData.get("Pet_tags_Tag_Name").toString();
        String petStatus = excelData.get("Pet_status").get(0);

       String insertStmt =
               "INSERT INTO PET_DETAILS\n" +
                       "(Test_Case_ID,Pet_id, Pet_Category_id, Pet_Category_name, Pet_name, Pet_photoUrls_photoUrl, Pet_tags_Tag_id, Pet_tags_Tag_Name, Pet_status)\n" +
                       " VALUES \n" +
                       "('"+testCaseID+"','"+petID+"','"+petCatID+"','"+petCatName+"','"+petName+"','"+petPhotoUrls+"','"+petTagID+"','"+petTagName+"','"+petStatus+"') ";
        return insertStmt;
    }//end creteInsertStatementOnPetDetails

    public static String selectLatestRecordFromPetDetails(String fieldName,String fieldValue){
        String selectStatement =
                "select * FROM PET_DETAILS\n" +
                        "WHERE ID = (SELECT max(ID) FROM PET_DETAILS WHERE "+fieldName+"='"+fieldValue+"')";


        return selectStatement;
    }


    }//end class SqlStatements
