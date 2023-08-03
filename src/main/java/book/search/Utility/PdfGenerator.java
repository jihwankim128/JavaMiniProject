package book.search.Utility;

import book.search.Model.Book;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class PdfGenerator {
    public static void generateBookListPdf(List<Book> books, String fileName) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(fileName);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setFontSize(12);
        PdfFont headerFont=null;
        PdfFont bodyFont=null;

        // font가 없을 경우 IOException 처리
        try {
            headerFont = PdfFontFactory.createFont("CookieRunBold.ttf", "Identity-H", true);
            bodyFont = PdfFontFactory.createFont("CookieRunRegular.ttf", "Identity-H", true);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        document.setFont(bodyFont);

        // Title 문단
        Paragraph titleParagraph = new Paragraph("도서 목록");
        titleParagraph.setFontSize(24);
        titleParagraph.setTextAlignment(TextAlignment.CENTER);
        titleParagraph.setBold();
        document.add(titleParagraph);

        // 테이블 생성, Title과 20p 떨어짐. width = [25][25][25][25]
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2}));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setMarginTop(20);

        // 테이블 헤더 추가
        table.addHeaderCell(createCell("제목", true, headerFont));
        table.addHeaderCell(createCell("저자", true, headerFont));
        table.addHeaderCell(createCell("출판사", true, headerFont));
        table.addHeaderCell(createCell("이미지", true, headerFont));

        // 도서 정보를 테이블에 추가
        for (Book book : books) {
            table.addCell(createCell(book.getTitle(), false, bodyFont));
            table.addCell(createCell(book.getAuthors(), false, bodyFont));
            table.addCell(createCell(book.getPublisher(), false, bodyFont));

            // 이미지 추가
            try {
                ImageData imageData = ImageDataFactory.create(book.getThumbnail());
                Image image = new Image(imageData);
                image.setAutoScale(true);
                table.addCell(new Cell().add(image).setPadding(5));
            } catch (MalformedURLException e) {
                table.addCell(createCell("이미지 불러오기 실패", false, bodyFont));
            }
        }

        document.add(table);
        document.close();
    }

    private static Cell createCell(String content, boolean isHeader, PdfFont font) {
        Paragraph paragraph = new Paragraph(content);
        Cell cell = new Cell().add(paragraph);
        cell.setPadding(5);
        cell.setFont(font);
        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            cell.setFontSize(14);
            cell.setBold();
        }
        return cell;
    }
}
