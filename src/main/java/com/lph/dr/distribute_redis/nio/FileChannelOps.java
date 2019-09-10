package com.lph.dr.distribute_redis.nio;

import com.lph.dr.distribute_redis.util.CsvUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class FileChannelOps {

    public static void main(String[] args) throws IOException {
        FileChannelOps fcChannelTest = new FileChannelOps();
        fcChannelTest.writeFile();
        fcChannelTest.readFile();
    }

    private void writeFile() {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File("D:\\hello12.txt"));
            FileChannel fileChannel = outputStream.getChannel();
            CharBuffer charBuffer = CharBuffer.allocate(1024);
            //往缓冲区存放数据
            charBuffer.put("hello world, 中转hg");
            //重设缓冲区
            charBuffer.flip();
            Charset charset = Charset.defaultCharset();
            ByteBuffer byteBuffer = charset.encode(charBuffer);

            //不能确定channel.write()能一次性写入buffer的所有数据
            //所以通过判断是否有余留循环写入
            while (byteBuffer.hasRemaining()) {
                fileChannel.write(byteBuffer);
            }

            fileChannel.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readFile() throws IOException {

        // 对文件的编码进行获取方便加以判断
        String charCode = CsvUtil.getTxtEncodeWithoutClose(new FileInputStream(new File("D:\\hello.txt")));
        System.err.println(charCode);
        // 文件编码是utf8,需要用utf8解码
        Charset charset = Charset.forName(charCode);
        CharsetDecoder decoder = charset.newDecoder();

        File file = new File("D:\\hello.txt");
        RandomAccessFile raFile = new RandomAccessFile(file, "rw");
        FileChannel fChannel = raFile.getChannel();

        ByteBuffer bBuf = ByteBuffer.allocate(32); // 缓存大小设置为32个字节。仅仅是测试用。
        CharBuffer cBuf = CharBuffer.allocate(32);

        int bytesRead = fChannel.read(bBuf); // 从文件通道读取字节到buffer.
        char[] tmp = null; // 临时存放转码后的字符
        byte[] remainByte = null;// 存放decode操作后未处理完的字节。decode仅仅转码尽可能多的字节，此次转码不了的字节需要缓存，下次再转
        int leftNum = 0; // 未转码的字节数
        while (bytesRead != -1) {

            bBuf.flip(); // 切换buffer从写模式到读模式
            decoder.decode(bBuf, cBuf, true); // 以utf8编码转换ByteBuffer到CharBuffer
            cBuf.flip(); // 切换buffer从写模式到读模式
            remainByte = null;
            leftNum = bBuf.limit() - bBuf.position();
            if (leftNum > 0) { // 记录未转换完的字节
                remainByte = new byte[leftNum];
                bBuf.get(remainByte, 0, leftNum);
            }

            // 输出已转换的字符
            tmp = new char[cBuf.length()];
            while (cBuf.hasRemaining()) {
                cBuf.get(tmp);
                System.out.print(new String(tmp));
            }

            bBuf.clear(); // 切换buffer从读模式到写模式
            cBuf.clear(); // 切换buffer从读模式到写模式
            if (remainByte != null) {
                bBuf.put(remainByte); // 将未转换完的字节写入bBuf，与下次读取的byte一起转换
            }
            bytesRead = fChannel.read(bBuf);
        }
        raFile.close();
    }
}
