package Netty.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFileWalkTree {
    public static void main(String[] args) throws IOException {
        AtomicInteger fileCount = new AtomicInteger();
        AtomicInteger dirCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("F:\\java"), new SimpleFileVisitor< Path >(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("======>"+dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(fileCount.intValue());
        System.out.println(dirCount.intValue());
    }
}
