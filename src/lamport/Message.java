package lamport;

public class Message {
    String message;
    Timer timer;
    String fromId;
//
//    public Message(String message, Timer timer) {
//        this.message = message;
//        this.timer = timer;
//    }

    public Message(String message, Timer timer, String fromId) {
        this.message = message;
        this.timer = timer;
        this.fromId = fromId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
