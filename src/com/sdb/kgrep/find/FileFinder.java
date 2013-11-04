/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdb.kgrep.find;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * glob又は正規表現でファイル、ディレクトリを検索するVisotor.
 *
 * @author debin.sun
 */
public class FileFinder extends SimpleFileVisitor<Path> {

    private static final Logger log = Logger.getLogger(FileFinder.class.getName());

    /**
     * 検索方法
     */
    public enum FindType {

        /**
         * 検索方法:glob
         */
        GLOB("glob:"),
        /**
         * 検索方法:正規表現
         */
        REGEX("regex:");

        /**
         * 検索パターン
         */
        private final String value;

        /**
         * コンストラクタ
         *
         * @param value 検索パターン
         */
        private FindType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
    /**
     * 検索のPathMatcher
     */
    private final List<PathMatcher> matchers;

    /**
     * 検査条件にあうファイル一覧
     */
    private final List<Path> files;

    /**
     * アクセス失敗したファイル
     */
    private List<Path> visitFailed;

    /**
     * 大文字・小文字の区別要否。<br/>
     * true:区別する。false:区別しない。
     */
    private final boolean casesensitive;

    /**
     * コンストラクタ。 大文字小文字を区別する。
     *
     * @param findType　検索方法
     * @param patterns　検索パターン
     */
    public FileFinder(FindType findType, String... patterns) {
        this(findType, true, patterns);
    }

    /**
     * コンストラクタ
     *
     * @param findType　検索方法
     * @param casesensitive 大文字・小文字の区別要否。true:区別する。false:区別しない。
     * @param patterns 検索パターン
     */
    public FileFinder(FindType findType, boolean casesensitive, String... patterns) {
        this.casesensitive = casesensitive;
        files = new ArrayList<Path>();
        matchers = new ArrayList<PathMatcher>();
        final FileSystem fs = FileSystems.getDefault();
        for (String pattern : patterns) {
            final String realPattern;
            if (casesensitive) {
                realPattern = pattern;
            } else {
                realPattern = pattern.toLowerCase();
            }
            matchers.add(fs.getPathMatcher(findType.toString() + realPattern));
        }
    }

    private void find(Path path) {
        if (path == null) {
            return;
        }
        final Path compPath;
        if (casesensitive) {
            compPath = path;
        } else {
            compPath = Paths.get(path.toFile().getAbsolutePath().toLowerCase());
        }
        for (PathMatcher matcher : matchers) {
            if (matcher.matches(compPath)) {
                files.add(path);
                break;
            }
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        visitFailed.add(file);
        log.log(Level.WARNING, "visit [" + file.toString() + "] failed.", exc);
        return CONTINUE;
    }

    /**
     * 見つけてファイルを取得する。
     *
     * @return 見つけたファイル一覧
     */
    public List<Path> getFound() {
        return files;
    }

    /**
     * アクセスできなかったファイルを取得する。
     *
     * @return アクセスできなかったファイル
     */
    public List<Path> getFailed() {
        return visitFailed;
    }
}
