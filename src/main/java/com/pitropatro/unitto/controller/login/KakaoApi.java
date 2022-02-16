package com.pitropatro.unitto.controller.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pitropatro.unitto.controller.login.oauthinterface.OauthApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class KakaoApi implements OauthApi {

    @Value("${kakao.kakao_id}")
    private String kakaoApiKey;

    public String getAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);    //타임아웃 5초
            factory.setReadTimeout(5000);

            RestTemplate restTemplate = new RestTemplate(factory);

            StringBuffer urlBuffer = new StringBuffer();
            // TODO : Redirect_uri 값 설정 방법 변경 필요
            urlBuffer.append(reqURL)
                    .append("?grant_type=authorization_code")
                    .append("&client_id=" + kakaoApiKey)
                    .append("&redirect_uri=http://localhost:8080/login/oauth2/code/kakao")
                    .append("&code=" + code);

            ResponseEntity<String> response = restTemplate.exchange(urlBuffer.toString(), HttpMethod.POST, null, String.class);

            System.out.println("status: " + response.getStatusCode());
            System.out.println("body: " + response.getBody());

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(response.getBody());

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    /*public String getAccessToken2(String code) {
        String accessToken = "";
        String refreshToken = "";
        // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+kakaoApiKey);
            // TODO : Redirect_uri 값 설정 방법 변경 필요
            sb.append("&redirect_uri=http://localhost:8080/login/oauth2/code/kakao");
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("response code = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body = " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }*/

    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
        String reqUrl = "https://kapi.kakao.com/v2/user/me";

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);    //타임아웃 5초
            factory.setReadTimeout(5000);

            RestTemplate restTemplate = new RestTemplate(factory);

            // Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> entity = new HttpEntity<>("", headers);

            ResponseEntity<String> response = restTemplate.exchange(reqUrl, HttpMethod.POST, entity, String.class);

            System.out.println("status: " + response.getStatusCode());
            System.out.println("body: " + response.getBody());

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(response.getBody());

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    /*public HashMap<String, Object> getUserInfo2(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body = " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }*/

    public void logout(String accessToken) {
        // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#logout
        String reqUrl = "https://kapi.kakao.com/v1/user/logout";

        try{
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);    //타임아웃 5초
            factory.setReadTimeout(5000);

            RestTemplate restTemplate = new RestTemplate(factory);

            // Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> entity = new HttpEntity<>("", headers);

            ResponseEntity<String> response = restTemplate.exchange(reqUrl, HttpMethod.POST, entity, String.class);

            System.out.println("status: " + response.getStatusCode());
            System.out.println("body: " + response.getBody());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*public void kakaoLogout2(String accessToken) {
        // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#logout
        String reqUrl = "https://kapi.kakao.com/v1/user/logout";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
