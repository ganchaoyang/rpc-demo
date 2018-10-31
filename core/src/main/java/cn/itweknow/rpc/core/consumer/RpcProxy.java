package cn.itweknow.rpc.core.consumer;

import cn.itweknow.rpc.core.model.ProviderInfo;
import cn.itweknow.rpc.core.model.RpcRequest;
import cn.itweknow.rpc.core.model.RpcResponse;
import cn.itweknow.rpc.core.registory.ServiceDiscovery;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author ganchaoyang
 * @date 2018/10/26 17:11
 * @description
 */
@Component
public class RpcProxy {


    private ServiceDiscovery serviceDiscovery;

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass, String providerName) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
            // 通过netty向Rpc服务发送请求。
            // 构建一个请求。
            RpcRequest request = new RpcRequest();
            request.setRequestId(UUID.randomUUID().toString())
                    .setClassName(method.getDeclaringClass().getName())
                    .setMethodName(method.getName())
                    .setParamTypes(method.getParameterTypes())
                    .setParams(args);
            ProviderInfo providerInfo = serviceDiscovery.discover(providerName);
            String[] addrInfo = providerInfo.getAddr().split(":");
            String host = addrInfo[0];
            int port = Integer.parseInt(addrInfo[1]);
            RpcClient rpcClient = new RpcClient(host, port);
            RpcResponse response = rpcClient.send(request);
            if (response.isError()) {
                throw response.getError();
            } else {
                return response.getResult();
            }
        });
    }

    public void setServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }
}
