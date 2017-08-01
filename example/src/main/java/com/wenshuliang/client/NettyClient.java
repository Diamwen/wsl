package com.wenshuliang.client;
/* 
 *  _   _  ____ __  __ __  __ 
 * | \ | |/ ___|  \/  |  \/  |
 * |  \| | |  _| |\/| | |\/| |
 * | |\  | |_| | |  | | |  | |
 * |_| \_|\____|_|  |_|_|  |_|                            
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wenshuliang on 2017/8/1.
 * Email:wenshuliang@ngmm365.com
 */
public class NettyClient {
    private final Bootstrap bootstrap;
    private final NioEventLoopGroup workGroup;
    private Map<String, ChannelFuture> channels = new ConcurrentHashMap<>();

    public NettyClient() {
        this.bootstrap = new Bootstrap();
        this.workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() + 1, new ThreadFactory() {
            private final AtomicInteger index = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Netty_Client_Work_" + index.incrementAndGet());
            }
        });
    }

    public NettyClient start(){
        bootstrap.group(workGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new StringEncoder(),
                                new StringDecoder(),
                                new NettyClientHandler());
                    }
                });
        return this;
    }


    public ChannelFuture connect(String addr, int port){
        ChannelFuture channel = channels.get(addr + port);
        if(channel != null){
            return channel;
        }
        channel = this.bootstrap.connect(new InetSocketAddress(addr, port));
        channels.put(addr + port, channel);
        return channel;
    }

    public void send(Channel channel, Object obj){
        channel.writeAndFlush(obj);
    }


    private class NettyClientHandler extends SimpleChannelInboundHandler{

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXX");
            System.out.println(msg);
        }
    }
}
