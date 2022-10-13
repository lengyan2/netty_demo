package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        // 1. 服务器端启动器 负责组装netty组件 启动服务器
        new ServerBootstrap()
                // 2. 加入一个组 有selector 和线程
                .group(new NioEventLoopGroup())
                // 3. channel NIO BIO  选择服务器的ServerSocketChannel
                .channel(NioServerSocketChannel.class)
                // 4. 告诉 worker 处理哪些事件  执行哪些操作
                .childHandler(
                        // 5 . 代表和客户端进行数据读写得到通信 初始化 负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 解码
                        nioSocketChannel.pipeline().addLast(new StringDecoder());

                        // 自定义handler
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            // 读事件
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // 绑定端口
                .bind(8080);
    }
}
