package com.pitropatro.unitto.controller.login.oauthinterface;

import java.util.HashMap;

public interface OauthApi {
<<<<<<< HEAD
    HashMap<String, Object> getTokenData(String code);
    HashMap<String, Object> getUserInfo(String accessToken);
    void logout(String accessToken);
=======
    String getAccessToken(String code);
    HashMap<String, Object> getUserInfo(String accessToken);
>>>>>>> f86225bc26a85439f8ad2fe996aebbc2e75ad535
}
