package com.pitropatro.unitto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/molly")
    public String main(){
        return "home.html";
    }
}
