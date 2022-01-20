package com.jzj.vueadmin.service;

import com.jzj.vueadmin.commin.dto.SysMenuDto;
import com.jzj.vueadmin.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jzj
 * @since 2022-01-14
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenuDto> getCurrentUserNav();
}
