package com.pitropatro.unitto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// TODO: 삭제할것
@Controller
public class MainController {

    @RequestMapping("/molly")
    public String main(){
        return "home.html";
    }
}
