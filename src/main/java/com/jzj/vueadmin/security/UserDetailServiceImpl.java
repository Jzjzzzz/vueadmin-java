package com.jzj.vueadmin.security;

import com.jzj.vueadmin.entity.SysUser;
import com.jzj.vueadmin.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Jzj
 * @Date 2022/1/18 18:52
 * @Version 1.0
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    SysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.getByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return new AccountUser(user.getId(),user.getUsername(),user.getPassword(),getUserAuthority(user.getId()));
    }

    /**
     * 获取用户权限信息
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(Long userId){
        //角色(ROLE_admin)、菜单操作权限(sys:user:list)
        String authority = userService.getUserAuthorityInfo(userId);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
