package com.airhacks.headlands.sync;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class SyncPresenter implements Initializable {

    @FXML
    TextField input;

    @FXML
    TextField output;

    @FXML
    Button syncButton;

    @FXML
    Label fromInfo;

    @Inject
    CacheService cacheService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanBinding fieldsAreEquals = input.textProperty().isEqualTo(output.textProperty());
        syncButton.disableProperty().bind(fieldsAreEquals);
    }

    public void sync() {
        String hostFrom = input.getText();
        String outputText = output.getText();
        cacheService.sync(fromInfo::setText, hostFrom, outputText);
    }

}
