package com.simple.admin.account.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.admin.account.api.IUserService;
import com.simple.admin.account.api.entity.User;
import com.simple.admin.account.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> listUser() {
        return userMapper.findUserList();
    }

    @Override
    public User getUserById(Integer id) {
        return getById(id);
    }
}
