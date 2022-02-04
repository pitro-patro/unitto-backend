package com.pitropatro.unitto.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login/oauth2/code")
public class LoginController {

    @RequestMapping(value = "/kakao", method = RequestMethod.GET)
    @ResponseBody
    public String kakaoOauthRedirect(@RequestParam String code){
        System.out.println("Kakao Login Done, code : "+code);
        return "카카오 로그인 인증 완료, code : " + code;
    }
}
