package com.jzj.vueadmin.security;

import cn.hutool.core.util.StrUtil;
import com.jzj.vueadmin.entity.SysUser;
import com.jzj.vueadmin.service.SysUserService;
import com.jzj.vueadmin.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Jzj
 * @Date 2022/1/18 18:23
 * @Version 1.0
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(jwtUtils.getHeader());
        if(StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);
            return;
        }
        Claims claims = jwtUtils.getClaimsByToken(jwt);
        if(claims == null){
            throw new JwtException("token 异常");
        }
        if(jwtUtils.isTokenExpired(claims)){
            throw new JwtException("token已过期");
        }
        String username = claims.getSubject();
        SysUser user = sysUserService.getByUserName(username);


        //获取用户的权限等信息
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(user.getId()));
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request,response);
    }
}
