package lamport;

/**
 * Logical Clock for Lamport's algorithm
 *
 * @author Ritik Kumar <ritikkne@gmail.com>
 */


public class LogicalClock extends Timer {
    int timer;

    public LogicalClock() {
        timer = 0;
    }

    public LogicalClock(int time) {
        timer = time;
    }

    @Override
    void increment(int processNo) {
        timer++;
    }

    @Override
    void decrement(int processNo) {
        timer--;
    }

    @Override
    void updateTimer(Timer receivedTimer, int processNo) {
        timer = Math.max(timer, ((LogicalClock) receivedTimer).timer);
        timer++;
    }

    @Override
    public String toString() {
        return String.valueOf(timer);
    }
}
