package nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * @author Administrator
 */
@Slf4j
public class WriteServer {

    public static void main(String[] args) throws IOException {
        //1.创建serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 2.创建一个selector
        Selector selector = Selector.open();

        // 3.绑定一个端口
        serverSocketChannel.bind(new InetSocketAddress(8080));

        //4. 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 5.ServerSocketChannel 注册到 selector 中
        SelectionKey key = serverSocketChannel.register(selector, 0, null);

        // 6 设置接收的事件 ,为ACCEPT

        key.interestOps(SelectionKey.OP_ACCEPT);

        // 7 . 写数据到channel中
        while (true){
            // 当有事件时 会恢复响应 没有事件时会阻塞
            selector.select();

            // 获取事件的集合
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                // 获取到事件，并且事件不会自动删除 所以要手动删除
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                // 判断事件是什么类型
                if (selectionKey.isAcceptable()) {
                     // r如果是accept类型 直接获取socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 往 channel中写数据
                    StringBuilder stringBuilder = new StringBuilder();
                    int length = 50000000;
                    for (int i = 0; i < length; i++) {
                        stringBuilder.append("a");
                    }
                    ByteBuffer buffer = StandardCharsets.UTF_8.encode(stringBuilder.toString());
                    log.info("写入channel的起始时间:{}", LocalDateTime.now());
                    // 判断buffer是否还有数据
                    while (buffer.hasRemaining()) {
                        // 写入的数据量
                        int write = socketChannel.write(buffer);
                        System.out.println(write);
                    }
                    log.info("写入完成channel的time：{}",LocalDateTime.now());
                }
            }

        }


    }
}
