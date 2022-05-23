package com.simple.admin.auth.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simple.admin.auth.entity.SysPermission;

import java.util.List;

public interface ISysPermissionService {

    List<SysPermission> listPermissionsByRoles(List<Integer> roleIds);
}
