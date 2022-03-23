package com.pitropatro.unitto.controller.login;

import com.pitropatro.unitto.controller.login.dto.UserSignInResponseDto;
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
    public UserSignInResponseDto kakaoLogin(@RequestParam("code") String code, HttpSession session){

        return userService.signIn(code, kakaoApi);
    }

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

    // TODO: 테스트 코드 (삭제 예정)
    @RequestMapping(value="/test")
    @ResponseBody
    public User test(@RequestParam("id") String id){
        return userRepository.getUserById(id);
    }

    @RequestMapping(value="/react-test")
    @ResponseBody
    public String reactTest(){
        return "Server Connected (Port 8080)";
    }
}
