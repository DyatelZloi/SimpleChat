package websocket;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import model.Message;

/**
 *
 * @author DiZi
 */
@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {
    
    @Inject
    private SessionHandler sessionHandler;
    
    @OnOpen
    public void open(Session session) {
        sessionHandler.addSession(session);
    }
    
    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }
    
    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }
    
    //TODO Паттерны используй.
    @OnMessage
    public void handleMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            if ("add".equals(jsonMessage.getString("action"))) {
                Message myMessage = new Message();
                myMessage.setMessage(jsonMessage.getString("message"));
                sessionHandler.addMessage(myMessage);
            }
            if ("remove".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.removeMessage(id);
            }
        }
    }
}
