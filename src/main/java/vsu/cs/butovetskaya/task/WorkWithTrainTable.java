package vsu.cs.butovetskaya.task;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class WorkWithTrainTable {

    public String[][] readTrainTable(String filename) throws IOException, InvalidFormatException {
        FileInputStream fis = new FileInputStream(filename);
        Workbook wb = new XSSFWorkbook(fis);
        String[][] result = new String[2000][9];
        Sheet sheet = wb.getSheetAt(0);
        int i = 0, j = 0;
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String r = "";
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        r = cell.getRichStringCellValue().getString();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        r = String.valueOf(cell.getNumericCellValue());
                        break;
                }
                result[i][j] = r;
                j++;
            }
            j = 0;
            i++;
            if (i == 2000) {
                break;
            }
        }
        fis.close();
        return result;
    }

    public String[][] correctTrainTableDiabetes(String[][] table) {
        for (int r = 1; r < table.length; r++) {
            if (Objects.equals(table[r][0], "Male")) {
                table[r][0] = "1";
            } else if (Objects.equals(table[r][0], "Female")) {
                table[r][0] = "0";
            }
            if (Objects.equals(table[r][4], "never") || Objects.equals(table[r][4], "No Info")) {
                table[r][4] = "0";
            } else if (Objects.equals(table[r][4], "former") || Objects.equals(table[r][4], "not current")) {
                table[r][4] = "1";
            } else if (Objects.equals(table[r][4], "ever") || Objects.equals(table[r][4], "current")) {
                table[r][4] = "2";
            }
        }
        String[][] readyTable = new String[table.length][table[0].length - 2];
        readyTable = table;

        return readyTable;
    }

}
