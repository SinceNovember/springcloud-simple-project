package com.simple.admin.auth.service;

import com.simple.admin.auth.entity.SysUser;

public interface ISysUserService {

    SysUser selectByUserName(String username);
}
