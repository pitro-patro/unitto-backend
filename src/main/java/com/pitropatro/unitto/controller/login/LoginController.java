package com.pitropatro.unitto.controller.login;

import com.pitropatro.unitto.exception.user.UserEmailNullException;
import com.pitropatro.unitto.repository.UserRepository;
import com.pitropatro.unitto.repository.dao.User;
import com.pitropatro.unitto.service.UserService;
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
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public LoginController(KakaoApi kakaoApi, UserRepository userRepository, UserService userService) {
        this.kakaoApi = kakaoApi;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String kakaoLoginPage(){
        return "kakaoLogin.html";
    }

    // Kakao redirect uri
    @RequestMapping(value = "/oauth2/code/kakao")
    @ResponseBody
    public String kakaoLogin(@RequestParam("code") String code, HttpSession session){

        userService.signIn(code, kakaoApi);

        return null;
    }

    /*@RequestMapping(value = "/oauth2/code/kakao2")
    @ResponseBody
    public ModelAndView kakaoLogin2(@RequestParam("code") String code, HttpSession session){
        ModelAndView mav = new ModelAndView();
        // 1번 인증코드 요청 전달
        String accessToken = kakaoApi.getAccessToken(code);

        // 2번 인증코드로 토큰 전달
        HashMap<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

        System.out.println("login info : " + userInfo.toString());

        if(userInfo.get("email") == null){
            throw new UserEmailNullException();
        }

        User user = userService.getUserByEmail(userInfo.get("email"));
        // 존재하지 않는 회원일 경우 회원가입
        if(user == null){
            user = userService.signUp(userInfo);
        }


        // 지울 내용
        if(userInfo.get("email") != null){
            session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("accessToken", accessToken);
        }
        mav.addObject("userId", userInfo.get("email"));
        mav.setViewName("kakaoLogin");
        return mav;
    }*/

    @RequestMapping(value="/logout")
    @ResponseBody
    public ModelAndView logout(HttpSession session){
        ModelAndView mav = new ModelAndView();

        kakaoApi.logout((String)session.getAttribute("accessToken"));
        session.removeAttribute("accessToken");
        session.removeAttribute("userId");
        mav.setViewName("kakaoLogin");
        return mav;
    }

    @RequestMapping(value="/test")
    @ResponseBody
    public User test(@RequestParam("email") String email){
        return userRepository.getUserByEmail(email);
    }
}
