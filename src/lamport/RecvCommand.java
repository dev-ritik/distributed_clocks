/*
author: Ritik Kumar
 */
package lamport;

public class RecvCommand extends Command {
    public static final String TYPE = "recv";
    private final Process recvFrom;
    private final String messageToRecv;
    private final Process myprocess;

    public RecvCommand(Process recvFrom, String messageToRecv, Process myprocess) {
        this.recvFrom = recvFrom;
        this.messageToRecv = messageToRecv;
        this.myprocess = myprocess;
    }

    public RecvCommand() {
        recvFrom = null;
        messageToRecv = "";
        myprocess = null;
    }

    @Override
    String getTYPE() {
        return TYPE;
    }

    @Override
    Process getMyprocess() {
        return null;
    }

    @Override
    Command validator(String[] input, Process myprocess, Process targetProcess) {
        if (input.length == 3) {
            return new RecvCommand(targetProcess, input[2], myprocess);
        } else {
            System.out.println("Error");
            return null;
        }
    }

    @Override
    int execute() {
        Message exp = myprocess.checkReceived(messageToRecv, recvFrom.id);
        if (exp != null) {
            myprocess.timer.decrement(myprocess.index);
            myprocess.timer.updateTimer(exp.timer, myprocess.index);
            System.out.println("received " + myprocess.id + ' ' + messageToRecv + ' ' + recvFrom.id + ' ' + myprocess.timer);
            return 0;
        } else {
//            System.out.println("Waiting for process " + recvFrom.id);
            myprocess.waitingFor = recvFrom;
            return 1;
        }
    }
}
