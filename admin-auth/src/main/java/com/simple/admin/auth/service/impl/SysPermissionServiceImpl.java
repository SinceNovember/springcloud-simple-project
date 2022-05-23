package com.simple.admin.auth.service.impl;

import com.simple.admin.auth.entity.SysPermission;
import com.simple.admin.auth.mapper.SysPermissionMapper;
import com.simple.admin.auth.service.ISysPermissionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class SysPermissionServiceImpl implements ISysPermissionService {

    @Resource
    private SysPermissionMapper permissionMapper;

    @Override
    public List<SysPermission> listPermissionsByRoles(List<Integer> roleIds) {
        return permissionMapper.selectBatchIds(roleIds);
    }
}
