package Netty.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TestFileChannelTransfer {
    public static void main(String[] args) {
        try {

            FileChannel from = new FileInputStream("data.txt").getChannel();
            FileChannel to = new FileOutputStream("to.txt").getChannel();
            // 传输最多2G 可以多次传输
            long size = from.size();
            for (long left=size;left>0;){
              left -=  from.transferTo(size-left,left,to);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
