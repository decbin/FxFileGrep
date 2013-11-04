package com.sdb.kgrep.read;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * テキストファイルを読む機能
 * @author debin.sun
 */
public class TextFileReader implements FileReadable {

    @Override
    public List<String> read(Path file, Charset charset) throws IOException {
        return Files.readAllLines(file, charset);
    }
}
