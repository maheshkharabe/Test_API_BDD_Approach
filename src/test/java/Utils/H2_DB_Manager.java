package Utils;

import Configs.TestConfigsForAPIPetStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class H2_DB_Manager {


    public static void createDBTable(String sqlStatementToCreateTable) {
        Connection conn=null;
        try {
            conn = DriverManager.getConnection
                    (TestConfigsForAPIPetStore.H2_DB_URL,
                            TestConfigsForAPIPetStore.H2_DB_USER,
                            TestConfigsForAPIPetStore.H2_DB_PASS);

            Statement stmt = conn.createStatement();
            System.out.println("Connecting to database and creating objects...");

            // 1. Create a table
            stmt.executeUpdate(sqlStatementToCreateTable);
            System.out.println("Created table in the database...");
            conn.close();

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("*******SQL Create Statement executed *******");
    }//end createDBTable

    public static void updateDBTable(String sqlStatement) {
        Connection conn=null;
        try {
            conn = DriverManager.getConnection
                    (TestConfigsForAPIPetStore.H2_DB_URL,
                            TestConfigsForAPIPetStore.H2_DB_USER,
                            TestConfigsForAPIPetStore.H2_DB_PASS);

            Statement stmt = conn.createStatement();
            System.out.println("Connecting to database...");

            stmt.executeUpdate(sqlStatement);
            System.out.println("Executing update statement...");
            conn.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("*******SQL Update Statement executed:"+sqlStatement);

    }//insertRecordToPetDetails

    public static Map<String, List<String>> selectDBTableRecord(String sqlStatement) {
        Map<String, List<String>> petRecordMap=new HashMap<>();
        Connection conn=null;
        try {
            conn = DriverManager.getConnection
                    (TestConfigsForAPIPetStore.H2_DB_URL,
                            TestConfigsForAPIPetStore.H2_DB_USER,
                            TestConfigsForAPIPetStore.H2_DB_PASS);

            Statement stmt = conn.createStatement();
            System.out.println("Connecting to database...");

            ResultSet rs = stmt.executeQuery(sqlStatement);
            while (rs.next()){
                petRecordMap.put("Pet_id", Arrays.asList(rs.getString("PET_ID")));
            }
            System.out.println("Executing statement...");
            conn.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("*******SQL Select Statement executed:"+sqlStatement);

        return petRecordMap;

    }//selectDBTableRecord

}//end class DB manager


