package lamport;

import java.util.ArrayList;
import java.util.List;

/**
 * Process class
 *
 * @author Ritik Kumar <ritikkne@gmail.com>
 */

public class Process {

    List<Command> commandsDue;
    List<String[]> inputCommands;
    Process blocked;
    String id;
    int index;
    List<Message> pool = new ArrayList<>();
    Timer timer;

    public Process(String id) {
        this.id = id;
        blocked = null;
        commandsDue = new ArrayList<>();
        inputCommands = new ArrayList<>();
    }

    static Process getOrCreate(String id) {
        return new Process(id);
    }

    boolean executeNext() {
        if (blocked != null) {
            return false;
        }
        if (commandsDue.isEmpty()) {
            return false;
        }
        Command current = commandsDue.get(0);
        timer.increment(index);
        int returnCode = current.execute();
        if (returnCode == 0) {
            commandsDue.remove(0);
            blocked = null;
            return true;
        } else {
            timer.decrement(index);
            return false;
        }
    }

    void handleIncommingMessage(Message message) {
        pool.add(message);
        if (blocked != null) {
            Command current = commandsDue.get(0);
            Timer orgTimer = getClonedTimer();
            timer.updateTimer(message.timer, index);
            int returnCode = current.execute();
            if (returnCode == 0) {
                commandsDue.remove(0);
                blocked = null;
            } else {
                if (timer instanceof LogicalClock) {
                    ((LogicalClock) timer).timer = ((LogicalClock) orgTimer).timer;
                } else if (timer instanceof VectorClock) {
                    ((VectorClock) timer).timer = ((VectorClock) orgTimer).timer;
                }
            }
        }
    }

    public void initiate(int totalProcesses, int index, boolean logicalTimer) {
        this.index = index;
        if (logicalTimer) {
            timer = new LogicalClock();
        } else {
            timer = new VectorClock(totalProcesses);
        }
    }

    public Timer getClonedTimer() {
        if (timer instanceof LogicalClock) {
            return new LogicalClock(((LogicalClock) timer).timer) {
                @Override
                void increment(int processIndex) {

                }

                @Override
                void decrement(int processIndex) {

                }

                @Override
                void updateTimer(Timer receivedTimer, int processNo) {

                }
            };
        } else {
            return new VectorClock(((VectorClock) timer).totalProcesses, ((VectorClock) timer).timer.clone()) {
                @Override
                void increment(int processIndex) {

                }

                @Override
                void decrement(int processIndex) {

                }

                @Override
                void updateTimer(Timer receivedTimer, int processNo) {

                }
            };
        }
    }

    Message checkReceived(String target, String fromId) {
        for (int i = 0; i < pool.size(); i++) {
            Message message = pool.get(i);
            if (message.payload.equals(target) && message.fromId.equals(fromId)) {
                pool.remove(i);
                return message;
            }
        }
        return null;
    }
}
