package com.sdb.kgrep.find;

import com.sdb.kgrep.FileInfo;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * ファイル検索を行うタスク
 *
 * @author debin.sun
 */
public class FileFindTask extends Task<ObservableList<FileInfo>> {

    private final Path start;
    private final String[] patterns;
    private final FileFinder.FindType type;
    private final ObservableList<FileInfo> files;
    /**
     * 大文字・小文字の区別要否。<br/>
     * true:区別する。false:区別しない。
     */
    private final boolean casesensitive;

    public FileFindTask(Path start, FileFinder.FindType type, String... patterns) {
        this(start, type, true, patterns);
    }

    public FileFindTask(Path start, FileFinder.FindType type, boolean casesensitive, String... patterns) {
        this.casesensitive = casesensitive;
        this.start = start;
        this.type = type;
        this.patterns = patterns;
        this.files = FXCollections.observableArrayList();
    }

    @Override
    protected ObservableList<FileInfo> call() throws Exception {
        final FileFinder finder = new FileFinder(type, casesensitive, patterns);
        Files.walkFileTree(start, finder);
        final List<Path> pathList = finder.getFound();
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                final List<FileInfo> fileList = new ArrayList<FileInfo>();
                for (Path path : pathList) {
                    fileList.add(createResult(path));
                }
                FileFindTask.this.files.setAll(fileList);
            }
        });
        return this.files;
    }

    public ObservableList<FileInfo> getFiles() {
        return files;
    }

    public static FileInfo createResult(Path path) {
        final File file = path.toFile();
        final String parent = file.getParent();
        final String fileName = file.getName();

        FileInfo result = new FileInfo();
        result.setPath(parent);
        result.setFileName(fileName);
        final int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            result.setExt(fileName.substring(dotIndex + 1));
        }
        result.setSize(file.length());
        result.setLastModified(file.lastModified());
        return result;
    }
}
