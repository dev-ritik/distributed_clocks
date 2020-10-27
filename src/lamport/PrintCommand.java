package lamport;

/**
 * Print message Command
 *
 * @author Ritik Kumar <ritikkne@gmail.com>
 */

public class PrintCommand extends Command {

    public static final String TYPE = "print";
    private Process myprocess;
    private String toprint;

    public PrintCommand() {
        myprocess = null;
    }

    @Override
    String getTYPE() {
        return TYPE;
    }

    @Override
    String getOutputTag() {
        return "printed";
    }

    @Override
    Process getMyprocess() {
        return null;
    }

    @Override
    Command validator(String[] input, Process myProcess, Process targetProcess) {
        if (input.length == 2) {
            toprint = input[1];
            myprocess = myProcess;
            return this;
        } else {
            System.out.println("Error");
            return null;
        }
    }

    @Override
    int execute() {
//        TODO: Output in output file
        System.out.println(getOutputTag() + ' ' + myprocess.id + ' ' + toprint + ' ' + myprocess.timer);
        return 0;
    }
}
