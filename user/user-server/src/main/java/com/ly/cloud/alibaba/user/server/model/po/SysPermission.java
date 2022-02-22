package com.ly.cloud.alibaba.user.server.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 权限实体类
 */
@Data
@Entity
@Table(name = "sys_permission")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysPermission implements Serializable {
    @Id
    private Long id;

    private String name;

    private String url;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
