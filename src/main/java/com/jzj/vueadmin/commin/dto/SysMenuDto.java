package com.jzj.vueadmin.commin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jzj
 * @Date 2022/1/21 2:31
 * @Version 1.0
 */
@Data
public class SysMenuDto implements Serializable {
    private Long id;
    private String title;
    private String icon;
    private String path;
    private String name;
    private String component;
    private List<SysMenuDto> children = new ArrayList<>();
}
