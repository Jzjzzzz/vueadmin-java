package com.jzj.vueadmin.security;

import com.jzj.vueadmin.commin.exception.CaptchaException;
import com.jzj.vueadmin.commin.lang.Const;
import com.jzj.vueadmin.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Jzj
 * @Date 2022/1/18 4:04
 * @Version 1.0
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();
        if("/login".equals(url) && httpServletRequest.getMethod().equals("POST")){
            //校验验证码
           try {
               validate(httpServletRequest);
           }catch (CaptchaException e){
               //交给认证失败处理器
               loginFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
           }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    //校验验证码逻辑
    private void validate(HttpServletRequest httpServletRequest) {
        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("token");
        if(StringUtils.isBlank(code) || StringUtils.isBlank(key)){
            throw new CaptchaException("验证码错误");
        }
        if(!code.equals(redisUtils.hget(Const.CAPTCHA_KEY,key))){
            throw new CaptchaException("验证码错误");
        }
        //清除redis中指定key的验证码
        redisUtils.hdel(Const.CAPTCHA_KEY,key);

    }
}
