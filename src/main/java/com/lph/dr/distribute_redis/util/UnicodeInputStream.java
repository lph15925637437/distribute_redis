package com.lph.dr.distribute_redis.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;


/**
 *
 * @author: lph
 * @date:  2019/6/18 9:14
 * @version V1.0
 */
public class UnicodeInputStream extends InputStream {

    PushbackInputStream internalIn;
    boolean isInited = false;
    String defaultEnc;
    String encoding;

    private static final int BOM_SIZE = 4;

    public UnicodeInputStream(InputStream in, String defaultEnc) {
        internalIn = new PushbackInputStream(in, BOM_SIZE);
        this.defaultEnc = defaultEnc;
    }

    public String getDefaultEncoding() {
        return defaultEnc;
    }

    public String getEncoding() {
        if (!isInited) {
            try {
                init();
            } catch (IOException ex) {
                IllegalStateException ise = new IllegalStateException("Init method failed.");
                ise.initCause(ise);
                throw ise;
            }
        }
        return encoding;
    }

    /**
     * Read-ahead four bytes and check for BOM marks. Extra bytes are unread
     * back to the stream, only BOM bytes are skipped.
     */
    protected void init() throws IOException {
        if (isInited)
            return;

        byte[] bom = new byte[BOM_SIZE];
        int n;
        int unread;
        n = internalIn.read(bom, 0, bom.length);

        if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
            encoding = "UTF-32BE";
            unread = n - 4;
        } else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00)
                && (bom[3] == (byte) 0x00)) {
            encoding = "UTF-32LE";
            unread = n - 4;
        } else if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
            encoding = "UTF-8";
            unread = n - 3;
        } else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
            encoding = "UTF-16BE";
            unread = n - 2;
        } else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
            encoding = "UTF-16LE";
            unread = n - 2;
        } else {
            // Unicode BOM mark not found, unread all bytes
            encoding = defaultEnc;
            unread = n;
        }

        if (unread > 0)
            internalIn.unread(bom, (n - unread), unread);

        isInited = true;
    }

    @Override
    public void close() throws IOException {
        isInited = true;
        internalIn.close();
    }

    public int read() throws IOException {
        isInited = true;
        return internalIn.read();
    }

}
