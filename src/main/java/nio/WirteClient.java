package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TransferQueue;

/**
 * @author Administrator
 */
public class WirteClient {

    public static void main(String[] args) throws IOException {
        // 接受服务器端 channel写入的数据
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 8080));

        // 多次接收
        int count = 0;
        while (true){
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            count += socketChannel.read(buffer);
            System.out.println(count);
        }
    }
}
