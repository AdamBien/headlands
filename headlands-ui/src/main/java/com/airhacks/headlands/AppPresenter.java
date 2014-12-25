package com.airhacks.headlands;

import com.airhacks.headlands.caches.CachesView;
import com.airhacks.headlands.entries.EntriesView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author airhacks.com
 */
public class AppPresenter implements Initializable {

    @FXML
    AnchorPane caches;

    @FXML
    AnchorPane entries;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CachesView cachesView = new CachesView();
        caches.getChildren().add(cachesView.getView());
        EntriesView entriesView = new EntriesView();
        entries.getChildren().add(entriesView.getView());
    }

}
