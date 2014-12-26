package com.airhacks.headlands.caches;

import com.airhacks.headlands.CacheAccessor;
import com.airhacks.headlands.HazelcastDiscoverer;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class CachesPresenter implements Initializable {

    @FXML
    ComboBox<String> caches;

    @Inject
    CacheAccessor accessor;

    @FXML
    Button startButton;

    @FXML
    Button stopButton;

    @FXML
    Button createCacheButton;

    @FXML
    TextField cacheNameField;

    @Inject
    HazelcastDiscoverer discoverer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanProperty started = this.accessor.isStarted();
        startButton.disableProperty().bind(started);
        stopButton.disableProperty().bind(started.not());
        cacheNameField.disableProperty().bind(started.not());
        createCacheButton.disableProperty().bind(started.not());
        caches.disableProperty().bind(started.not());
        caches.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            System.out.println("newValue = " + newValue);
            refreshCaches();
        });
    }

    public void selectCache() {
        String selectedItem = caches.getSelectionModel().getSelectedItem();
        System.out.println("selectedItem = " + selectedItem);
        this.accessor.selectCache(selectedItem);

    }

    void refreshCaches() {
        List<String> cacheNames = discoverer.getMapNames();
        ObservableList<String> observableList = FXCollections.observableList(cacheNames);
        caches.setItems(observableList);
    }

    public void createCache() {
        String cacheName = cacheNameField.getText();
        if (cacheName != null) {
            this.accessor.createCache(cacheName);
        }
    }

    public void start() {
        this.accessor.start();
    }

    public void stop() {
        this.accessor.stop();
    }

}
