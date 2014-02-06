package com.sdb.kgrep.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ファイルの文字コードをチェックするクラス。
 *
 * @author debin.sun
 */
public class CharsetDecoder {

    private static final Logger logger = Logger.getLogger(CharsetDecoder.class.getName());
    private final String fileName;

    public CharsetDecoder(String fileName) {
        this.fileName = fileName;
    }

    public Charset detect() {
        final File file = new File(fileName);
        InputStreamReader r = null;
        try {
            r = new InputStreamReader(new FileInputStream(file));
            final String charset = r.getEncoding();
            return Charset.forName(charset);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "close file:" + fileName + " fail!", e);
                }
            }
        }
    }
}
