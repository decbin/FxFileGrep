package com.sdb.kgrep.property;

import java.nio.file.Path;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Path情報を格納するプロパティ
 * @author debin.sun
 */
public class PathProperty extends SimpleObjectProperty<Path> {

    public PathProperty() {
    }

    public PathProperty(Path t) {
        super(t);
    }

    public PathProperty(Object o, String string) {
        super(o, string);
    }

    public PathProperty(Object o, String string, Path t) {
        super(o, string, t);
    }


}
