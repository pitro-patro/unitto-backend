package com.pitropatro.unitto.controller.login;

import com.pitropatro.unitto.repository.MysqlRepository;
import com.pitropatro.unitto.repository.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final KakaoApi kakaoApi;
    private final MysqlRepository mysqlRepository;

    @Autowired
    public LoginController(KakaoApi kakaoApi, MysqlRepository mysqlRepository) {
        this.kakaoApi = kakaoApi;
        this.mysqlRepository = mysqlRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String kakaoLoginPage(){
        return "kakaoLogin.html";
    }

    @RequestMapping(value = "/oauth2/code/kakao")
    @ResponseBody
    public ModelAndView kakaoLogin(@RequestParam("code") String code, HttpSession session){
        ModelAndView mav = new ModelAndView();
        // 1번 인증코드 요청 전달
        String accessToken = kakaoApi.getAccessToken(code);

        // 2번 인증코드로 토큰 전달
        HashMap<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

        System.out.println("login info : " + userInfo.toString());

        if(userInfo.get("email") != null){
            session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("accessToken", accessToken);
        }
        mav.addObject("userId", userInfo.get("email"));
        mav.setViewName("kakaoLogin");
        return mav;
    }

    @RequestMapping(value="/logout")
    @ResponseBody
    public ModelAndView logout(HttpSession session){
        ModelAndView mav = new ModelAndView();

        kakaoApi.kakaoLogout((String)session.getAttribute("accessToken"));
        session.removeAttribute("accessToken");
        session.removeAttribute("userId");
        mav.setViewName("kakaoLogin");
        return mav;
    }

    @RequestMapping(value="/test")
    @ResponseBody
    public User test(@RequestParam("email") String email){
        return mysqlRepository.getUserByEmail(email);
    }
}
