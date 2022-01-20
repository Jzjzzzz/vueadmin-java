package com.jzj.vueadmin.service;

import com.jzj.vueadmin.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jzj
 * @since 2022-01-14
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUserName(String username);

    String getUserAuthorityInfo(Long userId);

    void clearUserAuthorityInfo(String username);

    void clearUserAuthorityInfoByRoleId(String roleId);

    void clearUserAuthorityInfoByMenuId(String menuId);
}
