package com.idreamshen.zebra.server;

import com.idreamshen.zebra.bean.BeanFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.lang.reflect.Method;
import java.util.Map;

public class RequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private Map<String, String> routers;

    public RequestHandler(Map<String, String> routers) {
        this.routers = routers;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        HttpMethod method = request.method();
        String methodName = method.name();
        String path = request.uri();

        System.out.println(method.name());
        System.out.println(path);

        String key = path + "." + methodName;
        if (routers.containsKey(key)) {
            String router = routers.get(key);
            String beanName = router.split("\\.")[0];
            String beanMethodName = router.split("\\.")[1];
            Object bean = BeanFactory.beans.get(beanName);
            Method beanMethod = bean.getClass().getDeclaredMethod(beanMethodName);
            String result = (String)beanMethod.invoke(bean);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.content().writeBytes(Unpooled.copiedBuffer(result.getBytes()));
            ctx.writeAndFlush(response);
            ctx.close();
        } else {
            System.out.println(String.format("path=%s, method=%s, no handler", path, methodName));
        }


    }

}
