package Netty.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TestByteBufferString {
    public static void main(String[] args) {
        //  字符串转为bytebuffer
        byte[] bytes = "hello".getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.put(bytes);

        // 2.Charset
        ByteBuffer buffer = StandardCharsets.UTF_8.encode("hello");

        //3.wrap
        ByteBuffer buffer1 = ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8));

        // buffer转为String
        String s = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(s);

    }
}
