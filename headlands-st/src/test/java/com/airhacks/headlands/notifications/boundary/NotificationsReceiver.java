package com.airhacks.headlands.notifications.boundary;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 *
 * @author airhacks.com
 */
public class NotificationsReceiver extends Endpoint {

    private Session session;

    private String message;

    private CountDownLatch latch;

    public NotificationsReceiver() {
        this.latch = new CountDownLatch(1);
    }

    @Override
    public void onOpen(Session session, EndpointConfig ec) {
        this.session = session;
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String msg) {
                message = msg;
                latch.countDown();
            }
        });
    }

    public String getMessage() {
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(NotificationsReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }

}
