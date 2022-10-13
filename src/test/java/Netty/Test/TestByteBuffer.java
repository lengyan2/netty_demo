package Netty.Test;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
@Slf4j
public class TestByteBuffer {

    public static void main(String[] args) {

        // FileChannel
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            // 准备缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            while (true){

                int len = channel.read(byteBuffer);
                log.debug("读取到的字节数：{}",len);
                if (len==-1){
                    break;
                }
                // 打印buffer的内容
                byteBuffer.flip(); // buffer 读模式
                while (byteBuffer.hasRemaining()) // 是否有剩余的
                {
                    byte b = byteBuffer.get();
                    log.debug("实际字节：{}",(char)b);
                }
                byteBuffer.clear(); // 写模式
            }
            // channel读取数据 写入buffer
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
