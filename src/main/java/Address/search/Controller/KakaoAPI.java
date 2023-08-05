package Address.search.Controller;

import Address.search.Model.AddressInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class KakaoAPI {
    private static final String API_KEY = "bea167ebcb6b918c78e4822c13e7aa60";
    private static final String API_BASE_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    public static List<AddressInfo> getAddressCoordinate(String address) throws IOException {
        // 자바에서 기본으로 제공되는 URLEncoder 클래스
        // 주소는 한글일 수도 있으므로 UTF-8로 인코딩 해야함.
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        String requestUrl = API_BASE_URL + "?query=" + encodedAddress;

        // apache에서 제공하는 HTTP 통신을 제공하는 인터페이스 생성
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(requestUrl);
        httpGet.setHeader("Authorization", "KakaoAK " + API_KEY);

        // try-with-resources 구문이라서 자동으로 CloseableHttp 인터페이스의 리소스가 해제됨.
        // response를 처리하기 위한 인터페이스
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            // response가 성공하면 해당 주소에서 Json을 파싱함.
            // Kakao address search REST API에서 doucment 부분이 정보를 가지고 있음.
            String responseBody = EntityUtils.toString(response.getEntity());
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
            JsonArray documents = jsonObject.getAsJsonArray("documents");

            // 주소 정보가 있을 경우 위도와 경도를 출력.
            if (documents.size() > 0) {
                List<AddressInfo> addressInfos = new ArrayList<>();
                for(int i = 0; i<documents.size(); i++) {
                    JsonObject document = documents.get(0).getAsJsonObject();
                    String addressName = document.get("address_name").getAsString();
                    double latitude = document.get("y").getAsDouble();
                    double longitude = document.get("x").getAsDouble();
                    AddressInfo addressInfo = new AddressInfo(addressName, latitude, longitude);
                    addressInfos.add(addressInfo);
                }
                return addressInfos;
            } else {
                return null;
            }
        }
    }
}
