package com.jzj.vueadmin.service.impl;

import com.jzj.vueadmin.commin.dto.SysMenuDto;
import com.jzj.vueadmin.entity.SysMenu;
import com.jzj.vueadmin.entity.SysUser;
import com.jzj.vueadmin.mapper.SysMenuMapper;
import com.jzj.vueadmin.mapper.SysUserMapper;
import com.jzj.vueadmin.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzj.vueadmin.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jzj
 * @since 2022-01-14
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getByUserName(username);
        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        List<SysMenu> menus = this.listByIds(menuIds);
        //转树状结构
        List<SysMenu> menuTree = buildTreeMenu(menus);
        //实体转DTO
        return convert(menuTree);
    }

    private List<SysMenuDto> convert(List<SysMenu> menuTree) {
        ArrayList<SysMenuDto> menuDtos = new ArrayList<>();
        menuTree.forEach(m->{
            SysMenuDto dto = new SysMenuDto();
            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setIcon(m.getIcon());
            dto.setPath(m.getPath());
            if (m.getChildren().size() > 0) {
                dto.setChildren(convert(m.getChildren()));
            }
            menuDtos.add(dto);
        });
        return menuDtos;
    }
    private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {
        ArrayList<SysMenu> finalMenus = new ArrayList<>();
        for (SysMenu menu : menus) {
            for (SysMenu e : menus) {
                if(menu.getId() == e.getParentId()){
                    menu.getChildren().add(e);
                }
            }
            if(menu.getParentId() == 0L){
                finalMenus.add(menu);
            }
        }
        return finalMenus;
    }
}
