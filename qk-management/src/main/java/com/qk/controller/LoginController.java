package com.qk.controller;

import com.qk.common.Result;
import com.qk.entity.LoginResultVo;
import com.qk.entity.User;
import com.qk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("用户登录请求: {}", user.getUsername());
        LoginResultVo loginResult = userService.login(user.getUsername(), user.getPassword());
        return Result.success(loginResult);
    }
}