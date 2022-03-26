package com.pitropatro.unitto.controller.user;

import com.pitropatro.unitto.aspect.TokenRequired;
import com.pitropatro.unitto.repository.dto.User;
import com.pitropatro.unitto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        // TODO: UserInfoDto 대신 Dao 리턴해도 되는지?
        return userInfo;
    }
}
