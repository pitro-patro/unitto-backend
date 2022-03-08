package com.pitropatro.unitto.controller.login.oauthinterface;

import java.util.HashMap;

public interface OauthApi {
    HashMap<String, Object> getTokenData(String code);
    HashMap<String, Object> getUserInfo(String accessToken);
    void logout(String accessToken);
}
