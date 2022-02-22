package com.ly.cloud.alibaba.user.server.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_role_permission")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRolePermission {
    @Id
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "permission_id")
    private Long permissionId;
}
