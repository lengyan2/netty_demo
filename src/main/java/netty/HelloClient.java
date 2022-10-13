package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author Administrator
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        new Bootstrap()
                // 添加Eventloop
                .group(new NioEventLoopGroup())
                // 选择客户端channel 实现
                .channel(NioSocketChannel.class)
                // 添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    // 建立连接后调用
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }


                })
                .connect("localhost",8080)
        // 链接到服务器
                .sync() // 阻塞方法 知道链接建立才会调用
                .channel()
                .writeAndFlush("hello world");
    }
}
