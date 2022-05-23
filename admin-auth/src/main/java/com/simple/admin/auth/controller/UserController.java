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
     * ��ȡ��Ȩ���û���Ϣ
     *
     * @param principal ��ǰ�û�
     * @return ��Ȩ��Ϣ
     */
    @GetMapping("current/get")
    public Principal user(Principal principal) {
        return principal;
    }
}