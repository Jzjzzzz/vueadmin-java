package com.jzj.vueadmin.controller;

import com.jzj.vueadmin.service.*;
import com.jzj.vueadmin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Jzj
 * @Date 2022/1/14 2:47
 * @Version 1.0
 */
public class BaseController {

    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysRoleMenuService sysRoleMenuService;

}
