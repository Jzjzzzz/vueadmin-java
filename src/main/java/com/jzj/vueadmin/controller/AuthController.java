package com.jzj.vueadmin.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.jzj.vueadmin.commin.lang.Const;
import com.jzj.vueadmin.commin.lang.Result;
import com.jzj.vueadmin.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

/**
 * @Author Jzj
 * @Date 2022/1/18 2:39
 * @Version 1.0
 */
@RestController
public class AuthController extends BaseController {
    @Autowired
    Producer producer;

    @GetMapping("/captcha")
    public Result captcha() throws IOException{
        String key = UUID.randomUUID().toString();
        String code = producer.createText();
        key = "aaaaa";
        code = "11111";
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str+encoder.encode(outputStream.toByteArray());
        redisUtils.hset(Const.CAPTCHA_KEY,key,code,120);
        return Result.succ(
                MapUtil.builder()
                        .put("token",key)
                        .put("captchaImg",base64Img)
                        .build()
        );

    }
    @GetMapping("sys/userInfo")
    public Result userInfo(Principal principal){
        SysUser sysUser = sysUserService.getByUserName(principal.getName());
        return Result.succ(MapUtil.builder()
                .put("id",sysUser.getId())
                .put("username",sysUser.getUsername())
                .put("avatar",sysUser.getAvatar())
                .put("created",sysUser.getCreated())
                .map());
    }
}
