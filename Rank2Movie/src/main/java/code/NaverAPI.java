package code;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.LinkOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NaverAPI {

    public String run() {
        String clientId = "ohObFCVBEXfNkFnMcgOL"; // 애플리케이션 클라이언트 아이디
        String clientSecret = "QN0obHoQVc"; // 애플리케이션 클라이언트 시크릿

        String apiUrl = "https://openapi.naver.com/v1/datalab/search";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        requestHeaders.put("Content-Type", "application/json");
        requestHeaders.put("charset", "UTF-8");

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);
        System.out.println(today);

        Date dDate = new Date();
        dDate = new Date(dDate.getTime()+(1000*60*60*24));
        SimpleDateFormat dSdf = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = dSdf.format(dDate);
        System.out.println(yesterday);


        String requestBody = "{\"startDate\":\""+ yesterday +"\"," +
                "\"endDate\":\""+ today + "\"," +
                "\"timeUnit\":\"date\"," +
                "\"keywordGroups\":[" +
                "{\"groupName\":\"1위\"," + "\"keywords\":[\"한산\",\"hansan\"]}," +
                "{\"groupName\":\"2위\"," + "\"keywords\":[\"탑건:메버릭\",\"탑건\",\"메버릭\"]}," +
                "{\"groupName\":\"3위\"," + "\"keywords\":[\"미니언즈2\",\"미니언즈\"]}," +
                "{\"groupName\":\"4위\"," + "\"keywords\":[\"명탐정코난\",\"코난\",\"할로윈의신부\"]}," +
                "{\"groupName\":\"5위\"," + "\"keywords\":[\"외계인\",\"외+계인\",\"외계+인\"]}]}";

        String responseBody = post(apiUrl, requestHeaders, requestBody);
        System.out.println(responseBody);
        return responseBody;
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String requestBody) {
        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(requestBody.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect(); // Connection을 재활용할 필요가 없는 프로세스일 경우
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body, StandardCharsets.UTF_8);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}