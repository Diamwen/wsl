package com.wenshuliang.server;
/* 
 *  _   _  ____ __  __ __  __ 
 * | \ | |/ ___|  \/  |  \/  |
 * |  \| | |  _| |\/| | |\/| |
 * | |\  | |_| | |  | | |  | |
 * |_| \_|\____|_|  |_|_|  |_|                            
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wenshuliang on 2017/8/1.
 * Email:wenshuliang@ngmm365.com
 */
public class NettyServer {
    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup workGroup;
    private final EventLoopGroup bossGroup;
    private final int port;

    public NettyServer(int port) {
        this.port = port;
        this.serverBootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(1, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Netty_Server_Boss_" + threadIndex.incrementAndGet());
            }
        });
        workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() + 1, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Netty_Server_Work_" + index.incrementAndGet());
            }
        });
    }

    public NettyServer start(){
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new StringEncoder(),
                                new StringDecoder(),
                                new NettyServerHandler());
                    }
                });
        return this;
    }

    public ChannelFuture bind(){
        return serverBootstrap.bind(port);
    }

    private class NettyServerHandler extends SimpleChannelInboundHandler {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("==========================");
            System.out.println(msg);
        }
    }
}
