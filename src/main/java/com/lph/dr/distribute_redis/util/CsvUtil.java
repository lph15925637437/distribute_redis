package com.lph.dr.distribute_redis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

public class CsvUtil {

    public static final Logger log = LoggerFactory.getLogger(CsvUtil.class);

    public static final String PATH = "D:\\hello.txt";

    public static String getTxtEncodeWithoutClose(InputStream in) {
        String dc = Charset.defaultCharset().name();
        UnicodeInputStream uin = new UnicodeInputStream(in, dc);
        if ("UTF-8".equals(uin.getEncoding())) {
            return "UTF-8";
        }
        byte[] head = new byte[3];
        String code = "GBK";
        try {
            in.read(head);
            if (head[0] == -1 && head[1] == -2)
                code = "UTF-16";
            if (head[0] == -2 && head[1] == -1)
                code = "Unicode";
            // 带BOM
            if (head[0] == -17 && head[1] == -69 && head[2] == -65)
                code = "UTF-8";
            if ("Unicode".equals(code)) {
                code = "UTF-16";
            }
            return code;
        }catch (IOException e) {
            log.info("读取资源失败", e);
        }
        return code;
    }

    public static void main(String[] args){
        File file = new File(PATH);
        try {
            InputStream inputStream = new FileInputStream(file);
            String txtEncode = getTxtEncodeWithoutClose(inputStream);
            System.err.println("文件的编码格式:" + txtEncode);
        } catch (FileNotFoundException e) {
            log.error("文件输入流转换出错", e);
        }

    }
}
