package com.jzj.vueadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzj.vueadmin.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jzj
 * @since 2022-01-14
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<Long> getNavMenuIds(Long userId);

    List<SysUser> listByMenuId(String menuId);
}
