package lamport;

/**
 * Send message Command
 *
 * @author Ritik Kumar <ritikkne@gmail.com>
 */

public class SendCommand extends Command {
    public static final String TYPE = "send";
    private Process sendTo;
    private String messageToSend;
    private Process myprocess;

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
    String getOutputTag() {
        return "sent";
    }

    @Override
    Process getMyprocess() {
        return null;
    }

    @Override
    Command validator(String[] input, Process myprocess, Process targetProcess) {
        if (input.length == 3) {
            sendTo = targetProcess;
            messageToSend = input[2];
            this.myprocess = myprocess;
            return this;
        } else {
            System.out.println("Error");
            return null;
        }
    }

    @Override
    int execute() {
        System.out.println(getOutputTag() + ' ' + myprocess.id + ' ' + messageToSend + ' ' + sendTo.id + ' ' + myprocess.timer);
        sendTo.handleIncommingMessage(new Message(messageToSend, myprocess.getClonedTimer(), myprocess.id));
        return 0;
    }
}
