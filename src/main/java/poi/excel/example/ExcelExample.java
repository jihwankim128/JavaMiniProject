package poi.excel.example;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelExample {
    public static void main(String[] args) {
        try {
            String url = "example.xlsx";
            FileInputStream file = new FileInputStream(new File(url));
            // File 객체 생성 -> FileInputStream 으로 전달
            // 메모리에 가상의 excel을 만들어야 가져올 수 있음.
            Workbook workbook = WorkbookFactory.create(file);
            // 메모리에 올리기 위한 형태(WorkBook)로 변경

            Sheet sheet = workbook.getSheetAt(0);
            // Excel WorkBook에서 첫 sheet부터 가져옴.
            for(Row row : sheet) {
                for(Cell cell : row) {
                    // cell Type 체크
                    switch (cell.getCellType()) {
                        // 숫자 형태로 받아올 경우, 날짜도 숫자로 인식
                        case NUMERIC:
                            // Cell이 Date 타입인지 체크
                            if(DateUtil.isCellDateFormatted(cell)) {
                                // java.util에서 Date API
                                Date dateValue = cell.getDateCellValue();
                                DateFormat dateFromat = new SimpleDateFormat("yyyy-mm-dd");
                                String formattedDate = dateFromat.format(dateValue);
                                System.out.print(formattedDate + "\t");
                            }
                            else {
                                double numericValue = cell.getNumericCellValue();
                                // 실수를 자리내림 했을 때 값이랑 같다면 정수임.
                                if(numericValue == Math.floor(numericValue)) {
                                    System.out.print((int)numericValue + "\t");
                                }
                                else {
                                    System.out.print(numericValue + "\t");
                                }
                            }
                            break;
                        // 문자열일 경우
                        case STRING:
                            String stringValue = cell.getStringCellValue();
                            System.out.print(stringValue + "\t");
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getCellType() + "\t");
                            break;
                        // 수식일 경우
                        case FORMULA:
                            String formulaValue = cell.getCellFormula();
                            System.out.print(formulaValue + "\t");
                            break;
                        case BLANK:
                            System.out.print("\t");
                            break;
                        default:
                            System.out.print("\t");
                    }
                }
                System.out.println();
            }
            file.close();
            System.out.println("Excel 데이터 추출 성공");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
