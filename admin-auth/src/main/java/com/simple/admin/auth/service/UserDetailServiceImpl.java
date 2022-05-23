package com.simple.admin.auth.service;

import com.simple.admin.auth.entity.SysPermission;
import com.simple.admin.auth.entity.SysRole;
import com.simple.admin.auth.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private ISysUserService userService;

    @Resource
    private ISysRoleService roleService;

    @Resource
    private ISysPermissionService permissionService;

    @Resource
    private PasswordEncoder passwordEncoder;

    public static final String ROLE_PREFIX = "ROLE_";

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

//        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>().lambda()
//                .eq(User::getUsername, s);
        System.out.println(passwordEncoder.encode("123456"));
        SysUser user = userService.selectByUserName(s);
        if (user != null) {
            //获取当前用户的所有角色
            List<SysRole> roleList = roleService.listRoleByUserId(user.getId());
            user.setRoleList(roleList.stream()
                    .map(SysRole::getRoleCode)
                    .collect(Collectors.toList()));
            List<Integer> roleIds = roleList.stream().map(SysRole::getId).collect(Collectors.toList());
            //获取所有角色的权限
            roleIds.add(2);
            List<SysPermission> permissionList = permissionService.listPermissionsByRoles(roleIds);
           //gateway网关授权模式，添加用户有权限的url
           user.setPermissionList(permissionList.stream().map(SysPermission::getUrl).collect(Collectors.toList()));

            //微服务授权模式，添加用户所有拥有的权限
//            user.setPermissionList(permissionList.stream().map(SysPermission::getPermission).collect(Collectors.toList()));

            //构建oauth2的用户
            return buildUserDetails(user);
        } else {
            throw new UsernameNotFoundException("用户[" + s + "]不存在");
        }
    }

    /**
     * 构建oAuth2用户，将角色和权限赋值给用户，角色使用ROLE_作为前缀
     * @param sysUser 系统用户
     * @return UserDetails
     */
    private UserDetails buildUserDetails(SysUser sysUser) {
        Set<String> authSet = new HashSet<>();
        List<String> roles = sysUser.getRoleList();
        if(!CollectionUtils.isEmpty(roles)){
            roles.forEach(item -> authSet.add(ROLE_PREFIX + item));
            authSet.addAll(sysUser.getPermissionList());
        }

        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(authSet.toArray(new String[0]));

        return new User(
                sysUser.getUsername(),
                sysUser.getPassword(),
                authorityList
        );
    }

}
