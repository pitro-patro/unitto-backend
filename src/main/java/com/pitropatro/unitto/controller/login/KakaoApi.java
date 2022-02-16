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

    @Override
    public HashMap<String, Object> getTokenData(String code) {
        HashMap<String, Object> token = new HashMap<>();
        String accessToken = "";
        String refreshToken = "";
        Long refreshTokenExpire;
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
            refreshTokenExpire = element.getAsJsonObject().get("refresh_token_expires_in").getAsLong();

            token.put("accessToken", accessToken);
            token.put("refreshToken", refreshToken);
            token.put("refreshTokenExpire", refreshTokenExpire);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
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

            userInfo.put("name", nickname);
            userInfo.put("email", email);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }


    @Override
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

}
