/*
author: Ritik Kumar
 */

package lamport;

public class SendCommand extends Command {
    public static final String TYPE = "send";
    private final Process sendTo;
    private final String messageToSend;
    private final Process myprocess;

    public SendCommand(Process sendTo, String messageToSend, Process myprocess) {
        this.sendTo = sendTo;
        this.messageToSend = messageToSend;
        this.myprocess = myprocess;
    }

    public SendCommand() {
        sendTo = null;
        messageToSend = "";
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
            return new SendCommand(targetProcess, input[2], myprocess);
        } else {
            System.out.println("Error");
            return null;
        }
    }

    @Override
    int execute() {
        System.out.println("sent " + myprocess.id + ' ' + messageToSend + ' ' + sendTo.id + ' ' + myprocess.timer);
        sendTo.handleIncommingMessage(new Message(messageToSend, myprocess.getClonedTimer(), myprocess.id));
        return 0;
    }
}
