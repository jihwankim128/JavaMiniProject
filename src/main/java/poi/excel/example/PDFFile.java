package poi.excel.example;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class PDFFile {

    // MalformedURLException - 이미지 URL의 주소가 잘못될 경우 예외처리
    // IOException - 파일 입출력이 잘못되는 경우, 즉 파일 이름에 문제 있을 때
    public static void main(String[] args) throws MalformedURLException, IOException {
        String dest = "book_table.pdf";
        new PDFFile().createPdf(dest);
    }
    public void createPdf(String dest) throws MalformedURLException, IOException {
        // Input Map으로 받음. O(logN)
        List<Map<String, String>> books = createDummyData();

        // Initialize PDF writer and PDF document
        // writer -> pdfdocument -> document
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        // Initialize fonts
        PdfFont headerFont = null;
        PdfFont bodyFont = null;
        try {
            headerFont = PdfFontFactory.createFont("CookieRunBold.ttf", "Identity-H", true);
            bodyFont = PdfFontFactory.createFont("CookieRunRegular.ttf", "Identity-H", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize table
        // 열의 너비 [][  ][  ][  ][  ][  ]
        float[] columnWidths = {1, 2, 2, 2, 2, 2};
        // columnWidths를 비율로 테이블 생성
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        // document가 A4이므로 테이블 너비가 A4너비를 모두 채운다는 의미
        table.setWidth(UnitValue.createPercentValue(100));

        // Initialize table header cells
        Cell headerCell1 = new Cell().add(new Paragraph("순번")).setFont(headerFont);
        Cell headerCell2 = new Cell().add(new Paragraph("제목")).setFont(headerFont);
        Cell headerCell3 = new Cell().add(new Paragraph("저자")).setFont(headerFont);
        Cell headerCell4 = new Cell().add(new Paragraph("출판사")).setFont(headerFont);
        Cell headerCell5 = new Cell().add(new Paragraph("출판일")).setFont(headerFont);
        Cell headerCell6 = new Cell().add(new Paragraph("이미지")).setFont(headerFont);
        table.addHeaderCell(headerCell1);
        table.addHeaderCell(headerCell2);
        table.addHeaderCell(headerCell3);
        table.addHeaderCell(headerCell4);
        table.addHeaderCell(headerCell5);
        table.addHeaderCell(headerCell6);

        // Add table body cells
        int rowNum = 1;
        for (Map<String, String> book : books) {
            String title = book.get("title");
            String authors = book.get("authors");
            String publisher = book.get("publisher");
            String publishedDate = book.get("publishedDate");
            String thumbnail= book.get("thumbnail");

            Cell rowNumCell = new Cell().add(new Paragraph(String.valueOf(rowNum))).setFont(bodyFont);
            Cell titleCell = new Cell().add(new Paragraph(title)).setFont(bodyFont);
            Cell authorsCell = new Cell().add(new Paragraph(authors)).setFont(bodyFont);
            Cell publisherCell = new Cell().add(new Paragraph(publisher)).setFont(bodyFont);
            Cell publishedDateCell = new Cell().add(new Paragraph(publishedDate)).setFont(bodyFont);

            table.addCell(rowNumCell);
            table.addCell(titleCell);
            table.addCell(authorsCell);
            table.addCell(publisherCell);
            table.addCell(publishedDateCell);

            // 이미지 URL 파싱
            // 작성한 String 객체 URL -> URI(통합 자원 식별자)로 변환 -> URL 주소로 변환
            // -> ImageData로 가공해서 Image 파일로 변환
            ImageData imageData = ImageDataFactory.create(new File(thumbnail).toURI().toURL());
            Image img = new Image(imageData);
            Cell imageCell = new Cell().add(img.setAutoScale(true));
            table.addCell(imageCell);
            rowNum++;
        }
        document.add(table);
        document.close();
    }

    // INPUT, not HashMap (Hash Table)
    // MAP (binary search Tree - asc)
    private static List<Map<String, String>> createDummyData() {
        List<Map<String, String>> books = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("책 개수를 입력하세요:");
        int bookCount = scanner.nextInt();
        scanner.nextLine(); // 개행 문자 제거
        for (int i = 1; i <= bookCount; i++) {
            Map<String, String> book = new HashMap<>();

            System.out.printf("\n[ %d번째 책 정보 입력 ]\n", i);
            System.out.print("제목:");
            book.put("title", scanner.nextLine());

            System.out.print("저자:");
            book.put("authors", scanner.nextLine());

            System.out.print("출판사:");
            book.put("publisher", scanner.nextLine());

            System.out.print("출판일(YYYY-MM-DD):");
            book.put("publishedDate", scanner.nextLine());

            System.out.print("썸네일 URL:");
            book.put("thumbnail", scanner.nextLine());

            System.out.println(book.get("thumbnail"));
            books.add(book);
        }
        scanner.close();
        return books;
    }
}
