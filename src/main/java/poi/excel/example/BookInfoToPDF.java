package poi.excel.example;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Year;
import java.util.HashMap;

public class BookInfoToPDF {
    public static void main(String[] args) {
        // HashMap {key : value}
        HashMap<String, String> bookInfo = new HashMap<>();
        bookInfo.put("title", "한글 자바");
        bookInfo.put("author", "홍길동");
        bookInfo.put("publisher", "한글 출판사");
        bookInfo.put("year", String.valueOf(Year.now().getValue()));
        bookInfo.put("price", "25000");
        bookInfo.put("pages", "400");

        try {
            String pdfName = "book_information.pdf";
            // PDF 생성을 위한 PdfWriter 객체 생성
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfName));

            // PdfWriter 객체를 사용하여 PdfDocument 객체 생성
            PdfDocument pdf = new PdfDocument(writer);

            // Document 객체 생성
            Document document = new Document(pdf);

            // 폰트 생성 및 추가
            // 한글 문자를 인코딩하기 위한 방법 IDENTITY_H
            // 한글 문자 -> 유니코드
            //font파일이 pdf에 포함됨.
            PdfFont font =
                    PdfFontFactory.
                            createFont(
                            "CookieRunRegular.ttf",
                            PdfEncodings.IDENTITY_H,
                            true);

            document.setFont(font);

            // 책 정보를 문단으로 생성하여 Document에 추가
            // paragraph = 문단
            for (String key : bookInfo.keySet()) {
                Paragraph paragraph = new Paragraph(key + ": " + bookInfo.get(key));
                document.add(paragraph);
            }

            // Document 닫기
            document.close();

            System.out.println("book_information.pdf 파일이 생성되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
