package lamport;

/**
 * Message Class
 *
 * @author Ritik Kumar <ritikkne@gmail.com>
 */

public class Message {
    String payload;
    Timer timer;
    String fromId;

    public Message(String payload, Timer timer, String fromId) {
        this.payload = payload;
        this.timer = timer;
        this.fromId = fromId;
    }
}
