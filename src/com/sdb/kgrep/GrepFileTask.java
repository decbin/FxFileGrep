package com.sdb.kgrep;

import com.sdb.kgrep.read.FileReadable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;

/**
 * ファイルの内容をgrepするタスク。
 *
 * @author debin.sun
 */
public class GrepFileTask extends Task<List<FileInfo>> {

    private static final Logger log = Logger.getLogger(GrepFileTask.class.getName());

    /**
     * 検索方法
     */
    public enum GrepType {

        /**
         * 検索方法:単語
         */
        WORD,
        /**
         * 検索方法:正規表現
         */
        REGEX;
    }
    /**
     * Grep結果
     */
    private final List<FileInfo> grepResults;
    /**
     * Grep対象ファイル
     */
    private final List<Path> files;
    /**
     * Grep方法
     */
    private final GrepType grepType;
    /**
     * Grepパターン
     */
    private final String[] patterns;

    /**
     * コンストラクタ
     *
     * @param files String... patterns
     * @param grepType Grep方法
     * @param patterns Grepパターン
     */
    public GrepFileTask(List<Path> files, GrepType grepType, String... patterns) {
        this.files = files;
        this.grepType = grepType;
        this.patterns = patterns;
        this.grepResults = FXCollections.observableArrayList();
    }

    @Override
    protected List<FileInfo> call() throws Exception {
        for (Path file : files) {
            grepFile(file);
        }
        return grepResults;
    }

    private static List<String> readFile(Path file, Charset charset) throws IOException {
        final ServiceLoader<FileReadable> loaders = ServiceLoader.load(FileReadable.class);
        final Iterator<FileReadable> iterator = loaders.iterator();
        while (iterator.hasNext()) {
            final FileReadable reader = iterator.next();
            final List<String> lines = reader.read(file, charset);
            if (lines != null) {
                return lines;
            }
        }
        return null;
    }

    private void grepFile(final Path file) {
        List<String> lines;
        try {
            lines = readFile(file, Charset.defaultCharset());
        } catch (IOException ex) {
            log.log(Level.WARNING, "read file:" + file.toString() + " fail!", ex);
            return;
        }
        if (lines == null) {
            return;
        }
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            if (isMatch(line)) {
                FileInfo entry = new FileInfo();
                final String absolutePath = file.toFile().getAbsolutePath();
                entry.fileNameProperty.set(absolutePath);
//                entry.lineNoProperty.set("" + (i + 1));
//                entry.contentsProperty.set(line);
                this.grepResults.add(entry);
            }
        }

    }

    private boolean isMatch(String message) {
        if (grepType.equals(GrepType.REGEX)) {
            for (String pattern : patterns) {
                if (message.matches(pattern)) {
                    return true;
                }
            }
            return false;
        } else if (grepType.equals(GrepType.WORD)) {
            for (String pattern : patterns) {
                if (message.indexOf(pattern) >= 0) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
