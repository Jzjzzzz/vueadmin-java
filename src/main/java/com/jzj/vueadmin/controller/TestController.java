package com.jzj.vueadmin.controller;

import com.jzj.vueadmin.commin.lang.Result;
import com.jzj.vueadmin.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Jzj
 * @Date 2022/1/14 2:51
 * @Version 1.0
 */
@RestController
public class TestController {
    @Autowired
    SysUserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public Result test(){

        return Result.succ(userService.list());
    }
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/pass")
    public Result pass(){
        String password = bCryptPasswordEncoder.encode("111111");
        boolean matches = bCryptPasswordEncoder.matches("111111", password);
        System.out.println(matches);
        return Result.succ(password);
    }
}
