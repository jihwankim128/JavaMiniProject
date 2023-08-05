package Address.search.StartModel;

import Address.search.Controller.KakaoAPI;
import Address.search.Model.AddressInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class KakaoMain {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("주소를 입력해주세요 : ");
            String address = reader.readLine();

            List<AddressInfo> addressInfos = KakaoAPI.getAddressCoordinate(address);
            String search = "\n\"" + address + "\"";
            System.out.println(search+"로 검색한 결과");

            int cnt = 1;
            if(addressInfos == null) {
                System.out.println("주소를 찾을 수 없습니다.");
            }
            else {
                for (AddressInfo addressInfo : addressInfos) {
                    System.out.println("\n" + (cnt++) + "번 째 정보입니다.");
                    System.out.println("상세 주소 : " + addressInfo.getAddress());
                    System.out.println("위도 : " + addressInfo.getLatitude());
                    System.out.println("경도 : " + addressInfo.getLongitude());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
