package book.search.Controller;

import book.search.Model.Book;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class kakaoBookApi {
    private static final String API_KEY = "bea167ebcb6b918c78e4822c13e7aa60";
    private static final String API_BASE_URL = "https://dapi.kakao.com/v3/search/book";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // try에서 request가 요청되지 않은 네트워크일 경우 IOException 처리가 된다.
    // 메서드 선언부에서 IOException일 경우 적절한 예외처리를 해줌.
    // catch는 내가 처리해주고 싶은 예외처리를 작성가능.
    public static List<Book> searchBooks(String title) throws IOException {
        // url 구성을 해주는 객체로 URL을 파싱해서 URL 객체형태로 build해줌.
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_BASE_URL).newBuilder();
        // url로 접속시 default가 https://dapi.kakao.com/v3/search/book?target=title
        // 우리는 title을 전달받아서 https://dapi.kakao.com/v3/search/book?target=title&query="title" 로 검색하게 됨.
        urlBuilder.addQueryParameter("query", title);

        // KAKAO API 요청을 위한 url
        // -H "Authorization: KakaoAK ${REST_API_KEY}"
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Authorization", "KakaoAK " + API_KEY)
                .build();

        // client OkHttpClient 인스턴스, 네트워크 요청을 처리하기 위한 객체
        // newCall(requestURL) http 요청을 실행하기위한 역할
        // excute http요청을 동기적으로 실행하고 response 객체로 응답을 받음.
        // try-with-resources로 처리가 끝나면 자동으로 자원할당해제
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Request failed: " + response);

            // KAKAO REST API DOCS를 찾아보면 내용이 나옴.
            // HTTP 요청이 성공하면 검색한 내용을 바탕으로 JSON 객체를 던져줌.
            // gson을 통해 Json Body부분{Meta, Document}을 문자 스트림으로 변환하여 Json 객체로 바꿈
            JsonObject jsonResponse = gson.fromJson(response.body().charStream(), JsonObject.class);
            // doucment에 책과 관련된 정보들이 제공되어 있으므로 documents만 가져옴.
            JsonArray documents = jsonResponse.getAsJsonArray("documents");

            List<Book> books = new ArrayList<>();
            // JsonPrimitive, JsonNull, JsonParser, JsonObject, JsonArray 등의 부모 JsonElement
            for (JsonElement document : documents) {
                // doucment 내용을 Object 형식으로 받음.
                JsonObject bookJson = document.getAsJsonObject();
                Book book = new Book(
                        bookJson.get("title").getAsString(),
                        bookJson.get("authors").getAsJsonArray().toString(),
                        bookJson.get("publisher").getAsString(),
                        bookJson.get("thumbnail").getAsString()
                );
                books.add(book);
            }
            return books;
        }
    }
}
