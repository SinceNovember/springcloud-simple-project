package com.simple.admin.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.simple.admin.auth.entity.SysRole;
import com.simple.admin.auth.mapper.SysRoleMapper;
import com.simple.admin.auth.service.ISysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Resource
    private SysRoleMapper roleMapper;

    @Override
    public List<SysRole> listRoleByUserId(Integer userId) {
        return roleMapper.selectBatchIds(Arrays.asList(userId));
    }
}
