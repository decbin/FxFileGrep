package com.sdb.kgrep.read;

import com.sdb.kgrep.FileInfo;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author debin.sun
 */
public interface FileReadable {
    public List<String> read(FileInfo file, Charset charset) throws Exception;
}
