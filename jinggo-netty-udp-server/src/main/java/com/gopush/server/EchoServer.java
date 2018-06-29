package com.gopush.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * 服务端主类
 * @author wangyj
 * @description
 * @create 2018-06-29 16:24
 **/
public class EchoServer {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        b.group(group).channel(NioDatagramChannel.class).handler(new EchoServerHadler());

        // 服务端监听在9999 端口
        b.bind(9999).sync().channel().closeFuture().await();
    }
}
