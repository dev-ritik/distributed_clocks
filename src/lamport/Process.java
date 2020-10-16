/*
author: Ritik Kumar
 */
package lamport;

import java.util.ArrayList;
import java.util.List;

public class Process {

    List<Command> commandsDue;
    List<String[]> inputCommands;
    Process waitingFor;
    String id;
    int index;
    List<Message> receivedMessage = new ArrayList<>();
    Timer timer;

    public Process(String id) {
        this.id = id;
        waitingFor = null;
        commandsDue = new ArrayList<>();
        inputCommands = new ArrayList<>();
    }

    static Process getOrCreate(String id) {
        return new Process(id);
    }

    boolean executeNext() {
        if (waitingFor != null) {
            return false;
        }
        Command current = commandsDue.get(0);
        timer.increment(index);
        int returnCode = current.execute();
        if (returnCode == 0) {
            commandsDue.remove(0);
            waitingFor = null;
            return true;
        } else {
            timer.decrement(index);
            return false;
        }
    }

    void handleIncommingMessage(Message message) {
        receivedMessage.add(message);
        if (waitingFor != null) {
            Command current = commandsDue.get(0);
            Timer orgTimer = getClonedTimer();
            timer.updateTimer(message.timer, index);
            int returnCode = current.execute();
            if (returnCode == 0) {
                commandsDue.remove(0);
                waitingFor = null;
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
        for (int i = 0; i < receivedMessage.size(); i++) {
            Message message = receivedMessage.get(i);
            if (message.message.equals(target) && message.fromId.equals(fromId)) {
                receivedMessage.remove(i);
                return message;
            }
        }
        return null;
    }
}
