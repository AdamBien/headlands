package com.airhacks.headlands.sync;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class SyncPresenter {

    @FXML
    TextField input;

    @FXML
    TextField output;

    @Inject
    CacheService cacheService;

    public void sync() {
        String inputText = input.getText();
        String outputText = output.getText();
        cacheService.sync(inputText, outputText);
    }

}
