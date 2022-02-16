package com.pitropatro.unitto.service;

<<<<<<< HEAD
import com.pitropatro.unitto.controller.login.oauthinterface.OauthApi;
import com.pitropatro.unitto.exception.user.UserEmailNullException;
import com.pitropatro.unitto.exception.user.UserSignUpFailedException;
=======
import com.pitropatro.unitto.controller.login.KakaoApi;
import com.pitropatro.unitto.controller.login.oauthinterface.OauthApi;
import com.pitropatro.unitto.exception.user.UserEmailNullException;
>>>>>>> f86225bc26a85439f8ad2fe996aebbc2e75ad535
import com.pitropatro.unitto.repository.UserRepository;
import com.pitropatro.unitto.repository.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signIn(String code, OauthApi oauthApi) {

        // 1번 인증코드 요청 전달
        HashMap<String,Object> tokenData = oauthApi.getTokenData(code);

        // 2번 인증코드로 토큰 전달
        HashMap<String, Object> userInfo = oauthApi.getUserInfo((String) tokenData.get("accessToken"));

        System.out.println("token data : " + tokenData);
        System.out.println("login info : " + userInfo.toString());

        if(userInfo.get("email") == null){
            throw new UserEmailNullException();
        }

        User user = userRepository.getUserByEmail((String) userInfo.get("email"));

        // 존재하지 않는 회원일 경우 회원가입
        if(user == null){
            if(userRepository.signUp(userInfo, tokenData)){
                user = userRepository.getUserByEmail((String) userInfo.get("email"));
            }
            else
                throw new UserSignUpFailedException();
        }

        return user;
    }


}
