package com.simple.admin.account.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.simple.admin.account.api.IUserService;
import com.simple.admin.account.api.entity.User;
import com.simple.admin.core.entity.Response;
import com.simple.admin.core.entity.ResponseData;
import com.simple.admin.logging.starter.annotation.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    public static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource
    private IUserService userService;

    @GetMapping
    @SysLog("获取用户")
    @SentinelResource(value = "listUser", blockHandler = "handleException", fallback = "fallbackException")
    public ResponseData listUser() {
        return Response.ok(userService.listUser());
    }

    @GetMapping("/{userId}")
    public ResponseData getUser(@PathVariable("userId") Integer userId) {
        return Response.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseData addUser(@RequestBody User user) {
        user.setCreateTime(Date.from(Instant.now()));
        userService.save(user);
        return Response.ok();
    }

    public ResponseData handleException(BlockException ex) {
        log.error("flow exception{}", ex.getClass().getCanonicalName());
        return Response.fail("达到了阈值了，不要在访问了");
    }

    public ResponseData fallbackException() {
        return Response.fail("服务被熔断了，不要调用!");
    }


}
