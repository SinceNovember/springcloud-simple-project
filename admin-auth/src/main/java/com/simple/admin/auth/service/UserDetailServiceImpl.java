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
            //��ȡ��ǰ�û������н�ɫ
            List<SysRole> roleList = roleService.listRoleByUserId(user.getId());
            user.setRoleList(roleList.stream()
                    .map(SysRole::getRoleCode)
                    .collect(Collectors.toList()));
            List<Integer> roleIds = roleList.stream().map(SysRole::getId).collect(Collectors.toList());
            //��ȡ���н�ɫ��Ȩ��
            roleIds.add(2);
            List<SysPermission> permissionList = permissionService.listPermissionsByRoles(roleIds);
           //gateway������Ȩģʽ������û���Ȩ�޵�url
           user.setPermissionList(permissionList.stream().map(SysPermission::getUrl).collect(Collectors.toList()));

            //΢������Ȩģʽ������û�����ӵ�е�Ȩ��
//            user.setPermissionList(permissionList.stream().map(SysPermission::getPermission).collect(Collectors.toList()));

            //����oauth2���û�
            return buildUserDetails(user);
        } else {
            throw new UsernameNotFoundException("�û�[" + s + "]������");
        }
    }

    /**
     * ����oAuth2�û�������ɫ��Ȩ�޸�ֵ���û�����ɫʹ��ROLE_��Ϊǰ׺
     * @param sysUser ϵͳ�û�
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
