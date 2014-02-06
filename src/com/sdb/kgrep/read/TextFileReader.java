package com.sdb.kgrep.read;

import com.sdb.kgrep.FileInfo;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

/**
 * テキストファイルを読む機能
 *
 * @author debin.sun
 */
public class TextFileReader implements FileReadable {

    private static final Logger logger = Logger.getLogger(TextFileReader.class.getName());

    @Override
    public List<String> read(FileInfo fileInfo, Charset charset) throws Exception {
        final String abPath = fileInfo.getPath() + File.separator + fileInfo.getFileName();
        return Files.readAllLines(Paths.get(abPath), charset);
    }
}
