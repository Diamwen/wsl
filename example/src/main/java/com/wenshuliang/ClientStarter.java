package com.wenshuliang;
/* 
 *  _   _  ____ __  __ __  __ 
 * | \ | |/ ___|  \/  |  \/  |
 * |  \| | |  _| |\/| | |\/| |
 * | |\  | |_| | |  | | |  | |
 * |_| \_|\____|_|  |_|_|  |_|                            
 */

import com.wenshuliang.client.NettyClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Created by wenshuliang on 2017/8/1.
 * Email:wenshuliang@ngmm365.com
 */
public class ClientStarter {
    public static void main(String[] s) {
        try {
            main0(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main0(String[] s) throws InterruptedException {
        NettyClient client = new NettyClient().start();
        ChannelFuture channelFuture = client.connect("127.0.0.1" , 20000);
        Channel channel = channelFuture.sync().channel();
        new Thread(() -> {
            for(int i=0; i<100000; i++) {
                client.send(channel, "Thread11111111___" + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            for(int i=0; i<100000; i++) {
                client.send(channel, "Thread2222222___" + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
