package poi.excel.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import poi.excel.entity.MemberDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExcelWriter {
    public static void main(String[] args) {
        // 시스템 입력
        Scanner scanner = new Scanner(System.in);
        List<MemberDTO> members = new ArrayList<>();

        boolean cont = false;

        while(true) {
            System.out.print("회원 정보를 작성 하시겠습니까? (Y/N) : ");
            char YorN = scanner.next().charAt(0);
            scanner.nextLine();
            if(YorN == 'Y') {
                cont = true;
                break;
            }
            if(YorN == 'N') {
                cont = false;
                break;
            }
        }

        while(cont) {
            System.out.print("이름을 입력하세요 : ");
            String name = scanner.nextLine();

            System.out.print("나이를 입력하세요 : ");
            int age = scanner.nextInt();
            scanner.nextLine();

            System.out.print("생년월일을 입력하세요 : ");
            String birthdate = scanner.nextLine();

            System.out.print("전화번호를 입력하세요 : ");
            String phone = scanner.nextLine();

            System.out.print("주소를 입력하세요 : ");
            String address = scanner.nextLine();

            System.out.print("결혼 여부를 입력하세요 (true/false) : ");
            boolean isMarried = scanner.nextBoolean();
            scanner.nextLine();

            MemberDTO member = new MemberDTO(name, age, birthdate, phone, address, isMarried);
            members.add(member);

            while(true) {
                System.out.print("계속 작성 하시겠습니까? (Y/N) : ");
                char YorN = scanner.next().charAt(0);
                scanner.nextLine();
                if(YorN == 'Y') {
                    cont = true;
                    break;
                }
                if(YorN == 'N') {
                    cont = false;
                    break;
                }
            }
        }

        scanner.close();
        if(members.size() == 0) {
            System.out.println("입력된 정보가 없으므로 종료하겠습니다.");
            System.exit(-1);
        }

        try {
            // xlsx 파일을 핸들링하는 XSSFworkbook 객체
            XSSFWorkbook workbook = new XSSFWorkbook();
            // sheet 생성
            Sheet sheet = workbook.createSheet("회원 정보");

            // row 헤더 생성 0번째 로우 = {이름, 나이, ... }
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("이름");
            headerRow.createCell(1).setCellValue("나이");
            headerRow.createCell(2).setCellValue("생년월일");
            headerRow.createCell(3).setCellValue("전화번호");
            headerRow.createCell(4).setCellValue("주소");
            headerRow.createCell(5).setCellValue("결혼여부");

            // header row 아래 data들을 생성 0~5번 cell
            for (int i = 0; i < members.size(); i++) {
                MemberDTO member = members.get(i);
                // 헤더가 0번째 idx이므로 data는 i+1
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(member.getName());
                row.createCell(1).setCellValue(member.getAge());
                row.createCell(2).setCellValue(member.getBirthdate());
                row.createCell(3).setCellValue(member.getPhone());
                row.createCell(4).setCellValue(member.getAddress());
                // 위, 아래와 같은 방식으로 데이터 작성가능.
                Cell marriedCell = row.createCell(5);
                marriedCell.setCellValue(member.isMarried());
            }

            // 새로운 File 객체를 만들어서 outputStream에 전달
            String filename = "members.xlsx";
            FileOutputStream outputStream = new FileOutputStream(new File(filename));

            // Stream 버퍼에 workbook 내용을 작성
            workbook.write(outputStream);
            workbook.close();
            System.out.println("정상적으로 엑셀 파일이 저장되었습니다.");
        }
        catch (IOException e) {
            System.out.println("엑셀 파일 저장 중 오류가 발생");
            e.printStackTrace();
        }
    }
}
