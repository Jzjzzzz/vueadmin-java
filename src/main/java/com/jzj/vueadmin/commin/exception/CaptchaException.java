package com.jzj.vueadmin.commin.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @Author Jzj
 * @Date 2022/1/18 5:00
 * @Version 1.0
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }
}
