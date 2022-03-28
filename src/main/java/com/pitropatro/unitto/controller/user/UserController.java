package com.pitropatro.unitto.controller.user;

import com.pitropatro.unitto.aspect.TokenRequired;
import com.pitropatro.unitto.repository.dto.ConfirmedUniqueNumber;
import com.pitropatro.unitto.repository.dto.User;
import com.pitropatro.unitto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @TokenRequired
    @RequestMapping(value="/my-info", method = RequestMethod.GET)
    public User getUserInfo(User userInfo){
        return userInfo;
    }

    @TokenRequired
    @RequestMapping(value="/my-number-info", method = RequestMethod.GET)
    public List<ConfirmedUniqueNumber> getUserConfirmedUniqueNumber(User userInfo){
        return userService.getUserConfirmedUniqueNumber(userInfo);
    }
}
