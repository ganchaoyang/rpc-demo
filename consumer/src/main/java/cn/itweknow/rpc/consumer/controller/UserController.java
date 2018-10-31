package cn.itweknow.rpc.consumer.controller;

import cn.itweknow.rpc.core.interfaces.RpcConsumer;
import cn.itweknow.rpc.rpcapi.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ganchaoyang
 * @date 2018/10/26 16:51
 * @description
 */
@RestController
@RequestMapping("")
public class UserController {

    @RpcConsumer(serverName = "provider")
    private UserService userService;

    @RequestMapping("/hello")
    public String hello() {
        return userService.say();
    }

}
