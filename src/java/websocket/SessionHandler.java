package websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import model.Message;

/**
 *
 * @author DiZi
 */
@ApplicationScoped
public class SessionHandler {
    private int messageId = 0;
    private final Set<Session> sessions = new HashSet();
    private final Set<Message> messages = new HashSet();

    void addSession(Session session) {
        sessions.add(session);
        for (Message message : messages) {
            JsonObject addMessage = createAddMessage(message);
            sendToSession(session, addMessage);
        }
    }

    void removeSession(Session session) {
        sessions.remove(session);
    }

    void addMessage(Message message) {
        message.setId(messageId);
        messages.add(message);
        messageId++;
        JsonObject addMessage = createAddMessage(message);
        sendToAllConnectedSessions(addMessage);
    }

    void removeMessage(int id) {
        Message message = getMessageById(id);
        if (message != null) {
            messages.remove(message);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "remove")
                    .add("id", id)
                    .build();
            sendToAllConnectedSessions(removeMessage);
        }
    }

    private JsonObject createAddMessage(Message message) {
        JsonProvider jsonProvider = JsonProvider.provider();
        JsonObject addMessage;
        addMessage = jsonProvider.createObjectBuilder()
                .add("action", "add")
                .add("id", message.getId())
                .add("message", message.getMessage())
                .build();
        return addMessage;
    }

    private void sendToAllConnectedSessions(JsonObject addMessage) {
        for (Session session : sessions) {
            sendToSession(session, addMessage);
        }
    }

    private void sendToSession(Session session, JsonObject addMessage) {
        try {
            session.getBasicRemote().sendText(addMessage.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Message getMessageById(int id) {
        for (Message message : messages) {
            if (message.getId() == id) {
                return message;
            }
        }
        return null;
    }
}
