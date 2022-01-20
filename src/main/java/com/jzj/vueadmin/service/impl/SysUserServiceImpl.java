package com.jzj.vueadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jzj.vueadmin.entity.SysMenu;
import com.jzj.vueadmin.entity.SysRole;
import com.jzj.vueadmin.entity.SysUser;
import com.jzj.vueadmin.mapper.SysUserMapper;
import com.jzj.vueadmin.service.SysMenuService;
import com.jzj.vueadmin.service.SysRoleService;
import com.jzj.vueadmin.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzj.vueadmin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jzj
 * @since 2022-01-14
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    RedisUtils redisUtils;

    @Override
    public SysUser getByUserName(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username",username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        SysUser sysUser = baseMapper.selectById(userId);
        String authority= "";
        if(redisUtils.hasKey("GrantedAuthority:"+sysUser.getUsername())){
            authority = (String)redisUtils.get("GrantedAuthority:"+sysUser.getUsername());
        }else {
            //获取角色
            List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
                    .inSql("id", "select role_id from sys_user_role where user_id =" + userId));
            if(roles.size()>0){
                String roleCodes = roles.stream().map(r ->"ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority =roleCodes.concat(",");
            }

            //获取菜单操作编码
            List<Long> menuIds = sysUserMapper.getNavMenuIds(userId);
            if(menuIds.size()>0){
                List<SysMenu> menus = sysMenuService.listByIds(menuIds);
                String menuPerms = menus.stream().map(r -> r.getPerms()).collect(Collectors.joining(","));
                authority = authority.concat(menuPerms);
            }
        }

        redisUtils.set("GrantedAuthority:"+sysUser.getUsername(),authority,60*60);
        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtils.del("GrantedAuthority:"+username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(String roleId) {
        List<SysUser> users = this.list(new QueryWrapper<SysUser>()
                .inSql("id", "select user_id from sys_user_role where role_id =" + roleId));
        users.forEach(u->{
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }


    @Override
    public void clearUserAuthorityInfoByMenuId(String menuId) {
        List<SysUser> users = sysUserMapper.listByMenuId(menuId);
        users.forEach(u->{
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }
}
