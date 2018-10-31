package cn.itweknow.rpc.provider.service;

import cn.itweknow.rpc.core.interfaces.RpcService;
import cn.itweknow.rpc.rpcapi.service.UserService;

/**
 * @author ganchaoyang
 * @date 2018/10/26 15:12
 * @description
 */
@RpcService(UserService.class)
public class UserServiceImpl implements UserService {


    @Override
    public String say() {
        return "Hello Rpc!";
    }
}
