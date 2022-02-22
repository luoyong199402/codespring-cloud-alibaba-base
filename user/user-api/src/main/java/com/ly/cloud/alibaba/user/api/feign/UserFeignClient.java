package com.ly.cloud.alibaba.user.api.feign;

import com.ly.cloud.alibaba.user.api.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", contextId = "userFeignClient", path = "/user")
public interface UserFeignClient {
    @GetMapping("/username/{username}")
    UserDTO getByUsername(@PathVariable("username") String username);
}
