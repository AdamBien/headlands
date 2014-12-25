package com.airhacks.headlands.entries;

import com.airhacks.headlands.CacheAccessor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class EntriesPresenter {

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

    public void start() {
        this.cs.start();
    }

    public void stop() {
        this.cs.stop();
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
