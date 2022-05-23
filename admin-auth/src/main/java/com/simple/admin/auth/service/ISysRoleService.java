package com.simple.admin.auth.service;

import com.simple.admin.auth.entity.SysRole;

import java.util.List;

public interface ISysRoleService {

    List<SysRole> listRoleByUserId(Integer userId);

}
