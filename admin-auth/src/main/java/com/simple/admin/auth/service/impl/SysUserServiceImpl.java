package com.simple.admin.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.simple.admin.auth.entity.SysUser;
import com.simple.admin.auth.mapper.SysUserMapper;
import com.simple.admin.auth.mapper.UserMapper;
import com.simple.admin.auth.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Resource
    private SysUserMapper sysUserMapper;



    @Override
    public SysUser selectByUserName(String username) {
        Wrapper wrapper = Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username);
        return sysUserMapper.selectOne(wrapper);
    }
}
