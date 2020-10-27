package lamport;

import java.util.Arrays;

/**
 * Vector Clock implementation
 *
 * @author Ritik Kumar <ritikkne@gmail.com>
 */
public class VectorClock extends Timer {
    int[] timer;
    int totalProcesses;

    public VectorClock(int size) {
        timer = new int[size];
        totalProcesses = size;
        Arrays.fill(timer, 0);
    }

    public VectorClock(int size, int[] timer) {
        this.timer = timer.clone();
        totalProcesses = size;
    }


    @Override
    void increment(int processNo) {
        timer[processNo]++;
    }

    @Override
    void decrement(int processNo) {
        timer[processNo]--;
    }

    @Override
    void updateTimer(Timer receivedTimer, int processNo) {
        timer[processNo]++;
        for (int i = 0; i < totalProcesses; i++) {
            timer[i] = Math.max(timer[i], ((VectorClock) receivedTimer).timer[i]);
        }
    }

    @Override
    public String toString() {
        if (timer == null) {
            return "";
        } else {
            StringBuilder toRet = new StringBuilder("[");
            for (int i = 0; i < totalProcesses; i++) {
                toRet.append(timer[i]).append(", ");
            }
            toRet.deleteCharAt(toRet.length() - 1);
            toRet.deleteCharAt(toRet.length() - 1);
            toRet.append(']');
            return toRet.toString();
        }
    }
}