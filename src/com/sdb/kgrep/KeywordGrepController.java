package com.sdb.kgrep;

import com.sdb.kgrep.find.FileFindTask;
import com.sdb.kgrep.find.FileFinder;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

/**
 *
 * @author debin.sun
 */
public class KeywordGrepController implements Initializable {

    private String recentDirPath = null;

    @FXML
    private TextField dirField;
    @FXML
    private TextField fileField;
    @FXML
    private CheckBox fileRegCheckBox;
    @FXML
    private CheckBox fileICaseCheckBox;
    @FXML
    private TextField keywordField;
    @FXML
    private CheckBox keywordRegCheckBox;
    @FXML
    private CheckBox keywordICaseCheckBox;    

    @FXML
    private TableView<FileInfo> table;

    /**
     * フォルダ名カラム
     */
    @FXML
    private TableColumn<FileInfo, String> pathCol;

    /**
     * ファイル名カラム
     */
    @FXML
    private TableColumn<FileInfo, String> fileNameCol;

    /**
     * 拡張子カラム
     */
    @FXML
    private TableColumn<FileInfo, String> extCol;

    /**
     * サイズカラム
     */
    @FXML
    private TableColumn<FileInfo, Long> sizeCol;

    /**
     * 更新日時カラム
     */
    @FXML
    private TableColumn<FileInfo, Long> lastModifiedCol;
    
    /**
     * 文字コード
     */
    @FXML
    private TableColumn<FileInfo, String> encodeCol;
    /**
     * 行数
     */
    @FXML
    private TableColumn<FileInfo, String> lineNoCol;
    /**
     * 行内容
     */
    @FXML
    private TableColumn<FileInfo, String> lineCol;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.pathCol.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("path"));
        this.fileNameCol.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("fileName"));
        this.extCol.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("ext"));
        this.sizeCol.setCellValueFactory(new PropertyValueFactory<FileInfo, Long>("size"));
        this.pathCol.setCellFactory(new Callback<TableColumn<FileInfo, String>, TableCell<FileInfo, String>>() {
            @Override
            public TableCell<FileInfo, String> call(TableColumn<FileInfo, String> p) {
                final String baseDir = dirField.getText();
                return new TableCell<FileInfo, String>() {
                    @Override
                    protected void updateItem(String data, boolean empty) {
                        super.updateItem(data, empty);
                        if (data == null) {
                            setText("");
                        } else {
                            if (data.startsWith(baseDir)) {
                                setText("~" + data.substring(baseDir.length()));
                            } else {
                                setText(data);
                            }
                        }
                    }
                };
            }
        });

        this.sizeCol.setCellFactory(new Callback<TableColumn<FileInfo, Long>, TableCell<FileInfo, Long>>() {
            @Override
            public TableCell<FileInfo, Long> call(TableColumn<FileInfo, Long> p) {
                return new TableCell<FileInfo, Long>() {
                    @Override
                    protected void updateItem(Long data, boolean empty) {
                        super.updateItem(data, empty);
                        if (data == null) {
                            setText("");
                        } else {
                            setText(NumberFormat.getInstance().format(data));
                        }
                    }
                };
            }
        });
        this.lastModifiedCol.setCellValueFactory(new PropertyValueFactory<FileInfo, Long>("lastModified"));
        this.lastModifiedCol.setCellFactory(new Callback<TableColumn<FileInfo, Long>, TableCell<FileInfo, Long>>() {
            @Override
            public TableCell<FileInfo, Long> call(TableColumn<FileInfo, Long> p) {
                return new TableCell<FileInfo, Long>() {
                    @Override
                    protected void updateItem(Long data, boolean empty) {
                        super.updateItem(data, empty);
                        if (data == null) {
                            setText("");
                        } else {
                            Date date = new Date(data);
                            setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
                        }
                    }
                };
            }
        });
    }

    /**
     * ファイル選択ボタンを押した場合実行される処理
     *
     * @param event イベント
     */
    @FXML
    private void handleBrowseAction(ActionEvent event) {
        final DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("フォルダを選択");
        if (recentDirPath == null) {
            recentDirPath = System.getProperty("user.home");
        }
        dc.setInitialDirectory(new File(recentDirPath));
        final File file = dc.showDialog(dirField.getScene().getWindow());
        if (file != null) {
            recentDirPath = file.getAbsolutePath();
            dirField.setText(recentDirPath);
        }
    }

    private static void setNodeError(Node node, String message) {
        node.setStyle("-fx-background-color: red;");
        Tooltip.install(node, new Tooltip(message));
    }

    private static void setNodeNormal(Node node) {
        node.setStyle(null);
        Tooltip.install(node, new Tooltip(null));
    }

    /**
     * ファイル検索ボタンを押した場合実行される処理
     *
     * @param event イベント
     */
    @FXML
    private void handleFileGrepAction(ActionEvent event) {

        // 検索先
        final String dirPath = dirField.getText();
        if (dirPath == null || dirPath.trim().isEmpty()) {
            setNodeError(dirField, "フォルダを指定してください。");
            return;
        }
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            setNodeError(dirField, "フォルダが存在しません");
            return;
        }
        setNodeNormal(dirField);

        // ファイル名
        String fileNameExp = fileField.getText();
        if (fileNameExp == null || fileNameExp.trim().isEmpty()) {
            setNodeError(fileField, "ファイルを指定してください。");
            return;
        }
        setNodeNormal(fileField);

        final FileFinder.FindType fileGrepType = fileRegCheckBox.isSelected() ? FileFinder.FindType.REGEX : FileFinder.FindType.GLOB;
        final boolean casesensitive = fileICaseCheckBox.isSelected();
        final String pattern = fileField.getText();
        final FileFindTask task = new FileFindTask(Paths.get(dirPath), fileGrepType, casesensitive, pattern);
        table.setItems(task.getFindResult());
        new Thread(task).start();
    }
    @FXML
    private void handleKeywordGrepAction(ActionEvent event) {
    }
}
