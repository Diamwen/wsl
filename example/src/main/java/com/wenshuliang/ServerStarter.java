package com.wenshuliang;
/* 
 *  _   _  ____ __  __ __  __ 
 * | \ | |/ ___|  \/  |  \/  |
 * |  \| | |  _| |\/| | |\/| |
 * | |\  | |_| | |  | | |  | |
 * |_| \_|\____|_|  |_|_|  |_|                            
 */

import com.wenshuliang.server.NettyServer;
import io.netty.channel.ChannelFuture;

/**
 * Created by wenshuliang on 2017/8/1.
 * Email:wenshuliang@ngmm365.com
 */
public class ServerStarter {
    public static void main(String[] s)  {
        try {
            ChannelFuture channelFuture = new NettyServer(20000).start().bind();
            channelFuture.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
