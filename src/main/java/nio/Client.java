package nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author Administrator
 */
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel open = SocketChannel.open();
        open.connect(new InetSocketAddress("localhost",8080));
        System.out.println("waiting...");
        System.out.println("请输入消息");
        Scanner scanner = new Scanner(System.in);
        while (true){
            String s = scanner.nextLine();
            if ("break".equalsIgnoreCase(s)){
                break;
            }
            open.write(Charset.defaultCharset().encode(s));

        }
    }
}
