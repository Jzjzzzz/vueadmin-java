package com.jzj.vueadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author Jzj
 * @Date 2022/1/14 2:49
 * @Version 1.0
 */
@Data
public class BaseEntity implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private LocalDateTime created;
    private LocalDateTime updated;

    private Integer statu;
}
