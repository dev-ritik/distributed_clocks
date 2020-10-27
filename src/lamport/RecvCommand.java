package lamport;

/**
 * Receive message Command
 *
 * @author Ritik Kumar <ritikkne@gmail.com>
 */


public class RecvCommand extends Command {
    public static final String TYPE = "recv";
    private final Process recvFrom;
    final String messageToRecv;
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
    String getOutputTag() {
        return "received";
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
            System.out.println(getOutputTag() + ' ' + myprocess.id + ' ' + messageToRecv + ' ' + recvFrom.id + ' ' + myprocess.timer);
            return 0;
        } else {
            myprocess.blocked = recvFrom;
            return 1;
        }
    }
}
