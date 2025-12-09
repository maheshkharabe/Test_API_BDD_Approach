package Utils;
import Configs.TestConfigsForAPIPetStore;
import io.cucumber.java.en.Given;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelDataManager {

    public String[] getDataFromExcel(String uniqueTestID,String sheetName){
        String[] dataSet = null;
        try {
           File excelFile = new File(TestConfigsForAPIPetStore.pathToExcelDataFile);
           FileInputStream fis = new FileInputStream(excelFile);
           XSSFWorkbook workbook = new XSSFWorkbook(fis);
           XSSFSheet sheet = workbook.getSheet(sheetName);
           int numberOfRows = sheet.getPhysicalNumberOfRows();
           int numberOfColumns = sheet.getRow(0).getLastCellNum();
           System.out.println("***** Data Sheet Name provided.... :" + sheetName);
           System.out.println("***** Searching for Test Case ID:" + uniqueTestID);
           //System.out.println("***** Number of Rows:"+numberOfRows+" \nNumber of columns:"+numberOfColumns);

           dataSet = new String[numberOfColumns];

           int rowNumberOfTestID = -1;
           boolean isTestIDFound = false;
           DataFormatter dataFormatter = new DataFormatter();//reads data as string
           for (int i = 1; i < numberOfRows; i++) {//go through reach row starting with row2

               String testID = dataFormatter.formatCellValue(sheet.getRow(i).getCell(0));//take first cell of each row
               if (uniqueTestID.equals(testID)) { //compare with test id to search for
                   isTestIDFound = true;
                   rowNumberOfTestID = i;
                   System.out.println("****** Test Case ID found @Row number:" + (rowNumberOfTestID + 1));
               }
               if (isTestIDFound) {//once matching test case found, store data in aray
                   for (int j = 0; j < numberOfColumns; j++) {
                       dataSet[j] = dataFormatter.formatCellValue(sheet.getRow(rowNumberOfTestID).getCell(j));
                   }
               }//end if for data found
           }//end for

           if (isTestIDFound == false) {
               System.out.println("*************** NO DATA FOUND IN EXCEL FOR PROVIDED TEST CASE ID ***************");
               Assert.assertTrue(isTestIDFound,"NO DATA FOUND IN EXCEL FOR PROVIDED TEST CASE ID");
           }

           workbook.close();
           fis.close();
       }catch (Exception e){
            System.err.println("Exception occurred while reading excel data...");
            System.out.println(e);
       }
        return dataSet;

    }//end getDataFromExcel

}//end class
