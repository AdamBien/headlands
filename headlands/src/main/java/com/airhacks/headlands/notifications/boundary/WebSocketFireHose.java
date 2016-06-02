package com.airhacks.headlands.notifications.boundary;

import com.airhacks.headlands.notifications.entity.CacheChangedEvent;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.json.JsonArray;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
@ServerEndpoint(value = "firehose", encoders = JsonArrayEncoder.class)
public class WebSocketFireHose {

    CopyOnWriteArrayList<Session> sessions;

    @PostConstruct
    public void init() {
        this.sessions = new CopyOnWriteArrayList<>();
    }

    @OnOpen
    public void onConnect(Session session) {
        this.sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        this.sessions.remove(session);
    }

    public void onChange(@Observes CacheChangedEvent event) {
        JsonArray payload = event.getPayload();
        sessions.forEach(s -> this.send(s, payload));
    }

    void send(Session session, JsonArray payload) {
        try {
            session.getBasicRemote().sendObject(payload);
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(WebSocketFireHose.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    public void shutdown() {
        this.sessions.forEach(s -> {
            try {
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(WebSocketFireHose.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
