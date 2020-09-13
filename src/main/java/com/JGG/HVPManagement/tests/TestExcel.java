package com.JGG.HVPManagement.tests;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestExcel {
    public static void main(String[] args) throws IOException {
        File src = new File("C:\\Users\\javie\\Dropbox\\JGG\\Estudios\\Code\\Projects\\HVPManagement\\src\\test.xlsx");
        FileInputStream fileInputStream = new FileInputStream(src);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        String string = sheet.getRow(0).getCell(1).getStringCellValue();
        System.out.println("Data from Excel "+string);

        workbook.close();

        System.out.println("Test2");
        fileInputStream = new FileInputStream(src);
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheetAt(0);
        System.out.println(sheet.getLastRowNum());
        int rowCount = sheet.getLastRowNum();
        //int columnCount = sheet.getcolum
        for(int i=0; i<rowCount; i++){

            //String sheet.getRow(i).getCell(0).getStringCellValue();
        }
        workbook.close();

        // test 3
        System.out.println("https://www.youtube.com/watch?v=6IigtmfpsmI&t=452s");
        Xls_Reader reader = new Xls_Reader("./src/test.xlsx");
        System.out.println(reader.getCellData("Hoja1", 1, 2));
        System.out.println(reader.getCellData("Hoja1", "Col1", 2));
        System.out.println(reader.getRowCount("Hoja1"));
        reader.setCellData("Hoja1", "América", 16, "Pepito");
        System.out.println(reader.isSheetExist("Hoja2"));
        //reader.addColumn("Hoja1", "NewCol");
        //reader.addSheet("Hoja2");
        System.out.println("getCellRowNum"+ reader.getCellRowNum("Hoja1", "América", "B2"));
        System.out.println("Column count"+reader.getColumnCount("Hoja1"));

        System.out.println("Getting a monetary number"+reader.getCellData("Hoja1", 3, 2));
        System.out.println("Getting a date"+reader.getCellData("Hoja1", 3, 3));
        System.out.println("Getting a time"+reader.getCellData("Hoja1", 3, 4));
        System.out.println("Getting a dateTime"+reader.getCellData("Hoja1", 3, 5));
        System.out.println("Getting a boolean"+reader.getCellData("Hoja1", 3, 6));
        System.out.println("Getting a formula"+reader.getCellData("Hoja1", 3, 7));
        System.out.println("Getting a double"+reader.getCellData("Hoja1", 3, 8));
        System.out.println("Getting a percentage"+reader.getCellData("Hoja1", 3, 9));
        System.out.println("Getting a String"+reader.getCellData("Hoja1", 3, 10));



        //WebDriverManager





    }
}
