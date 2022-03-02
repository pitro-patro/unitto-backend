package com.pitropatro.unitto.service;

import com.pitropatro.unitto.controller.login.dto.UserSignInResponseDto;
import com.pitropatro.unitto.controller.login.oauthinterface.OauthApi;
import com.pitropatro.unitto.exception.token.EmptyTokenException;
import com.pitropatro.unitto.exception.user.UserEmailNullException;
import com.pitropatro.unitto.exception.user.UserSignUpFailedException;
import com.pitropatro.unitto.repository.UserRepository;
import com.pitropatro.unitto.repository.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public UserSignInResponseDto signIn(String code, OauthApi oauthApi) {

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
            else{
                throw new UserSignUpFailedException();
            }
        }

        return new UserSignInResponseDto(user.getEmail(), user.getName(), tokenService.createJwtToken(user.getId()));
    }

    public User getUserInfoByToken(String bearerToken){

        if(!StringUtils.hasLength(bearerToken)){
            throw new EmptyTokenException();
        }

        String jwtToken = bearerToken.substring("Bearer ".length());

        Map<String, Object> jwtClaims = tokenService.verifyJwtAndReturnClaims(jwtToken);
        String userId = jwtClaims.get("id").toString();

        return userRepository.getUserById(userId);
    }


}
