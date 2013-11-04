package com.sdb.kgrep.read;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author debin.sun
 */
public interface FileReadable {
    public List<String> read(Path file, Charset charset) throws IOException;
}
