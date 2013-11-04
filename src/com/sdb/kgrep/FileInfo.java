package com.sdb.kgrep;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 検索結果情報を保持するクラス
 *
 * @author debin.sun
 */
public class FileInfo {

    /**
     * フォルダ名
     */
    public StringProperty pathProperty = new SimpleStringProperty(this, "フォルダ名", null);

    /**
     * ファイル名
     */
    public StringProperty fileNameProperty = new SimpleStringProperty(this, "ファイル名", null);

    /**
     * 拡張子
     */
    public StringProperty extProperty = new SimpleStringProperty(this, "拡張子", null);

    /**
     * サイズ
     */
    public LongProperty sizeProperty = new SimpleLongProperty(this, "サイズ(M)", 0L);

    /**
     * 更新日時
     */
    public LongProperty lastModifiedProperty = new SimpleLongProperty(this, "更新日時", 0L);

//    /**
//     * 行数
//     */
//    public StringProperty lineNoProperty = new SimpleStringProperty(this, "行数", null);
//
//    /**
//     * 内容
//     */
//    public StringProperty contentsProperty = new SimpleStringProperty(this, "内容", null);

    public String getPath() {
        return pathProperty.get();
    }

    public void setPath(String path) {
        this.pathProperty.set(path);
    }

    public String getExt() {
        return extProperty.get();
    }

    public void setExt(String ext) {
        this.extProperty.set(ext);
    }

    public String getFileName() {
        return fileNameProperty.get();
    }

    public void setFileName(String fileName) {
        this.fileNameProperty.set(fileName);
    }

    public Long getSize() {
        return sizeProperty.get();
    }

    public void setSize(Long size) {
        this.sizeProperty.set(size);
    }

    public Long getLastModified() {
        return lastModifiedProperty.get();
    }

    public void setLastModified(Long lastModified) {
        this.lastModifiedProperty.set(lastModified);
    }
//
//    public String getLineNo() {
//        return lineNoProperty.get();
//    }
//
//    public void setLineNo(String lineNo) {
//        this.lineNoProperty.set(lineNo);
//    }
//
//    public String getContents() {
//        return contentsProperty.get();
//    }
//
//    public void setContents(String contents) {
//        this.contentsProperty.set(contents);
//    }
}
