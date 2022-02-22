package com.ly.cloud.alibaba.user.server.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 角色实体类
 */
@Data
@Entity
@Table(name = "sys_role")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRole {
    @Id
    private Long id;

    private String name;

    private String code;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
