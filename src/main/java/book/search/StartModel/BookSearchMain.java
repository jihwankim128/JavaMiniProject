package book.search.StartModel;

import book.search.Controller.kakaoBookApi;
import book.search.Model.Book;
import book.search.Utility.PdfGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class BookSearchMain {
    public static void main(String[] args) {
        // kakaoBookApi.searchBooks 에서 IOException이 발생하게 되면
        // 호출자인 main에서 IOException을 catch하고 오류처리를 함.
        try {
            Scanner scanner=new Scanner(System.in);
            System.out.print("도서제목을 입력하세요:");
            String bookTitle = scanner.nextLine();
            List<Book> books = kakaoBookApi.searchBooks(bookTitle);

            if (books.isEmpty()) {
                System.out.println("검색 결과가 없습니다.");
            } else {
                for(Book book : books){
                    System.out.println(book);
                }

                String fileName = "도서목록.pdf";
                PdfGenerator.generateBookListPdf(books, fileName);
                System.out.println(fileName + " 파일이 생성되었습니다.");

            }
        } catch (IOException e) {
            System.err.println("에러가 발생했습니다: " + e.getMessage());
        }
    }
}
