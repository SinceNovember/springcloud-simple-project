package com.simple.admin.auth.controller;

import com.simple.admin.auth.entity.User;
import com.simple.admin.auth.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    public UserMapper userMapper;



    @GetMapping("getByName")
    public User getByName() {
        return userMapper.selectByUserName("zhangjian");
    }

    /**
     * 获取授权的用户信息
     *
     * @param principal 当前用户
     * @return 授权信息
     */
    @GetMapping("current/get")
    public Principal user(Principal principal) {
        return principal;
    }
}