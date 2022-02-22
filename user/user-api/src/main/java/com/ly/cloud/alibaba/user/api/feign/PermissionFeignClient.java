package com.ly.cloud.alibaba.user.api.feign;

import com.ly.cloud.alibaba.user.api.model.dto.PermissionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
@FeignClient(name = "user", contextId = "permissionFeignClient", path = "/permission")
public interface PermissionFeignClient {
    @GetMapping("/role-code/{roleCode}")
    List<PermissionDTO> listByRoleCode(@PathVariable("roleCode") String roleCode);
}
