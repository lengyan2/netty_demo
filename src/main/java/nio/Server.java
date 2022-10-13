package nio;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author daiyuanjing
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        // 使用NIO来理解阻塞
        // 创建selector 管理多个channel
        Selector selector = Selector.open();

        ByteBuffer buffer = ByteBuffer.allocate(16);

        //1. 创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //设置非阻塞
        ssc.configureBlocking(false);
        // 建立selector与channel的联系 selectionKey 事件发生后得到事件 和channel
        SelectionKey key = ssc.register(selector, 0, null);
        key.interestOps(SelectionKey.OP_ACCEPT);
        // 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));

        // accept建立链接
        while (true){
            // 调用selector,没有事件发生 线程阻塞 ，有事件 线程恢复运行
            selector.select();
            // 处理事件 事件未处理时 不会阻塞
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                log.info("key:{}",selectionKey);
                iterator.remove(); // selector在发生事件后 会向集合中加入key 但是不会删除
                // 区分事件类型
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel channel =(ServerSocketChannel) selectionKey.channel();
                    SocketChannel sc = channel.accept();
                    log.info("accept...");
                    // 接受链接  设置非阻塞  注册selector
                    sc.configureBlocking(false);
                    SelectionKey scReadKey = sc.register(selector, 0, null);
                    scReadKey.interestOps(SelectionKey.OP_READ);
                }else if (selectionKey.isReadable()){
                    try {
                       SocketChannel readChannel =(SocketChannel) selectionKey.channel();
                        int read = readChannel.read(buffer);
                        if (read==-1){
                            selectionKey.cancel();
                        }else {
                            buffer.flip();
                            String message = StandardCharsets.UTF_8.decode(buffer).toString();
                            System.out.println("接受到的消息:"+message);
                            buffer.clear();
                        }
                   } catch (IOException e){
                       // 断开链接相当与一个读事件 当客户端断开时 无法读到数据 引发异常 要将这个key删除
                       e.printStackTrace();
//                       selectionKey.cancel();
                   }

                }

            }
        }
    }
}
