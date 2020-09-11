package com.JGG.HVPManagement.tests;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestExcel {
    public static void main(String[] args) throws FileNotFoundException {
        File excel = new File("a.xlsx");
        FileInputStream fileInputStream = new FileInputStream(excel);

        XSSFWorkbook wb = new XSSFWorkbook(fis)

        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

    }
}
