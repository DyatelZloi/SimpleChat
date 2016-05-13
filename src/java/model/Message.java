package model;

/**
 *
 * @author DiZi
 */
public class Message {
    int id;
    String message;

    public void setMessage(String message) {
       this.message = message;
    }

    public void setId(int messageId) {
        this.id = messageId;
    }

    public int getId() {
        return id;
    }
    
    public String getMessage(){
        return message;
    }
}
