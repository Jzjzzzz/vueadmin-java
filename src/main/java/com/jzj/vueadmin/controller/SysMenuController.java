package com.jzj.vueadmin.controller;


import cn.hutool.core.map.MapUtil;
import com.jzj.vueadmin.commin.dto.SysMenuDto;
import com.jzj.vueadmin.commin.lang.Result;
import com.jzj.vueadmin.entity.SysUser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jzj
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

    @GetMapping("/nav")
    public Result nav(Principal principal){
        SysUser sysUser = sysUserService.getByUserName(principal.getName());
        //获取权限信息
        String authorityInfo = sysUserService.getUserAuthorityInfo(sysUser.getId());
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");
        //获取导航栏信息
        List<SysMenuDto> navs = sysMenuService.getCurrentUserNav();
        return Result.succ(MapUtil.builder()
                .put("authoritys",authorityInfoArray)
                .put("nav",navs)
                .map());
    }



}
