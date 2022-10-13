package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author Administrator
 */
public class MutilThreadServer {
    public static void main(String[] args) throws IOException {
        //1. 创建serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 创建selector
        Selector boss = Selector.open();
        // serversocketchannel注册进selector
        SelectionKey bosskey = serverSocketChannel.register(boss, 0, null);
        // 设置接受事件
        bosskey.interestOps(SelectionKey.OP_ACCEPT);
        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8080));

        Worker worker = new Worker("worker-0");
        worker.register();
        while (true){
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()){
                    SocketChannel channel = serverSocketChannel.accept();

                    channel.configureBlocking(false);

                    channel.register(worker.worker,SelectionKey.OP_READ,null);

                }
            }

        }

    }

static class Worker implements  Runnable{
    private Thread thread;
    private  Selector worker;
    private String name;
    private volatile boolean start = false ;// 还未初始化

    public Worker(String name){
        this.name = name;
    }
    public void register() throws IOException {
        if (!start){
            worker = Selector.open();
            thread = new Thread(this,name);
            thread.start();
            start = true;
        }

    }


    @Override
    public void run() {
        while (true){
            try {
                worker.select();
                worker.wakeup();
                Iterator<SelectionKey> iterator = worker.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        SocketChannel channel = (SocketChannel) key.channel();
                        channel.read(buffer);
                        buffer.flip();
                        String message = StandardCharsets.UTF_8.decode(buffer).toString();
                        System.out.println(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
}
