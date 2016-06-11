package com.airhacks.headlands.notifications.boundary;

import com.airhacks.headlands.notifications.entity.CacheChangedEvent;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.json.JsonArray;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author airhacks.com
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@ServerEndpoint(value = "/firehose/{cache-name}", encoders = JsonArrayEncoder.class)
public class WebSocketFirehose {

    CopyOnWriteArrayList<Session> sessions;

    @PostConstruct
    public void init() {
        this.sessions = new CopyOnWriteArrayList<>();
    }

    @OnOpen
    public void onConnect(Session session, @PathParam("cache-name") String cacheName) {
        System.out.println("uri: " + session.getRequestURI().toString());
        this.sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        this.sessions.remove(session);
    }

    public void onChange(@Observes CacheChangedEvent event) {
        JsonArray payload = event.getPayload();
        sessions.stream().
                filter(s -> s.isOpen()).
                filter(s -> s.getRequestURI().toString().endsWith("*")
                || s.getRequestURI().toString().endsWith(event.getCacheName())).
                forEach(s -> this.send(s, payload));
    }

    void send(Session session, JsonArray payload) {
        try {
            session.getBasicRemote().sendObject(payload);
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(WebSocketFirehose.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    public void shutdown() {
        this.sessions.forEach(s -> {
            try {
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(WebSocketFirehose.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
