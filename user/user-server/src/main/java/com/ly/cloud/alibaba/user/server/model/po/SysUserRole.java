package com.ly.cloud.alibaba.user.server.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_user_role")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRole {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;
}
