package com.sdb.kgrep.grep;

import com.sdb.kgrep.FileInfo;
import com.sdb.kgrep.read.FileReadable;
import com.sdb.kgrep.util.CharsetDecoder;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * ファイルの内容をgrepするタスク。
 *
 * @author debin.sun
 */
public class FileGrepTask extends Task<ObservableList<FileInfo>> {

    private static final Logger log = Logger.getLogger(FileGrepTask.class.getName());

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
    private final ObservableList<FileInfo> grepResults;
    /**
     * Grep対象ファイル
     */
    private final List<FileInfo> fileInfos;
    /**
     * Grep方法
     */
    private final GrepType grepType;
    /**
     * Grepパターン
     */
    private final String pattern;

    /**
     * コンストラクタ
     *
     * @param fileInfos Grep対象ファイル
     * @param grepType Grep方法
     * @param patterns Grep条件のパターン
     */
    public FileGrepTask(List<FileInfo> fileInfos, GrepType grepType, String pattern) {
        this.fileInfos = fileInfos;
        this.grepType = grepType;
        this.pattern = pattern;
        this.grepResults = FXCollections.observableArrayList();
    }

    @Override
    protected ObservableList<FileInfo> call() throws Exception {
        final List<FileInfo> files = new ArrayList<FileInfo>();
        for (FileInfo file : fileInfos) {
            grepFile(file, files);
        }
        this.grepResults.setAll(files);
        return grepResults;
    }

    private void grepFile(final FileInfo fileInfo, final List<FileInfo> files) {
        final String filePath = fileInfo.getPath() + File.separator + fileInfo.getFileName();
        final Charset charset = new CharsetDecoder(filePath).detect();
        List<String> lines;
        try {
            lines = readFile(fileInfo, charset);
        } catch (Exception ex) {
            log.log(Level.WARNING, "read file:" + fileInfo.toString() + " fail!", ex);
            FileInfo entry = fileInfo.copy();
            entry.setEncode("Unknown");
            files.add(entry);
            return;
        }
        if (lines == null) {
            return;
        }
        for (int i = 1; i <= lines.size(); i++) {
            final String line = lines.get(i - 1);
            if (isMatch(line)) {
                FileInfo entry = fileInfo.copy();
                entry.setLineNo("" + i);
                entry.setLine(line);
                entry.setEncode(charset.displayName());
                files.add(entry);
            }
        }
    }

    private static List<String> readFile(FileInfo fileInfo, Charset charset) throws Exception {
        final ServiceLoader<FileReadable> loaders = ServiceLoader.load(FileReadable.class);
        final Iterator<FileReadable> iterator = loaders.iterator();
        while (iterator.hasNext()) {
            final FileReadable reader = iterator.next();
            final List<String> lines = reader.read(fileInfo, charset);
            if (lines != null) {
                return lines;
            }
        }
        return null;
    }

    private boolean isMatch(String message) {
        if (grepType.equals(GrepType.REGEX)) {
            if (message.matches(pattern)) {
                return true;
            }
            return false;
        } else if (grepType.equals(GrepType.WORD)) {
            if (message.indexOf(pattern) >= 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public ObservableList<FileInfo> getGrepResults() {
        return grepResults;
    }

}
