package com.pitropatro.unitto.controller.login.oauthinterface;

import java.util.HashMap;

public interface OauthApi {
    String getAccessToken(String code);
    HashMap<String, Object> getUserInfo(String accessToken);
}
