package com.airhacks.headlands.entries;

import com.airhacks.headlands.CacheAccessor;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import javax.cache.Cache;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class EntriesPresenter implements Initializable {

    @Inject
    CacheAccessor cs;

    @FXML
    TextField key;

    @FXML
    TextField queryKey;

    @FXML
    TextField value;

    @FXML
    Label result;

    @FXML
    TableView cacheContent;

    @FXML
    TableColumn<Pair, String> keyColumn;

    @FXML
    TableColumn<Pair, String> valueColumn;

    ObservableList<Pair<String, String>> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.data = FXCollections.observableArrayList();
        setupTable();
        setupListener();
    }

    public void setupListener() {
        this.cs.currentCacheProperty().addListener((o, oldvalue, newvalue) -> {
            refreshContents();
        });
    }

    private void refreshContents() {
        List<Cache.Entry<String, String>> allEntries = this.cs.getAllEntries();
        List<Pair<String, String>> collect = allEntries.stream().map(e -> new Pair<>(e.getKey(), e.getValue())).collect(Collectors.toList());
        this.data = FXCollections.observableList(collect);
        this.cacheContent.setItems(data);

    }

    private void setupTable() {
        keyColumn.setCellValueFactory(
                new PropertyValueFactory<>("key"));
        valueColumn.setCellValueFactory(
                new PropertyValueFactory<>("value"));

        cacheContent.setItems(this.data);
    }

    public void store() {
        String keyString = key.getText();
        String valueString = value.getText();
        this.cs.store(keyString, valueString);
    }

    public void retrieve() {
        String key = this.queryKey.getText();
        String result = this.cs.getValue(key);
        this.result.setText(result);
    }

    public void remove() {
        String key = this.queryKey.getText();
        this.cs.remove(key);
    }

}
