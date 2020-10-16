/*
author: Ritik Kumar
 */

package lamport;

public class PrintCommand extends Command {

    public static final String TYPE = "print";
    private final Process myprocess;
    private String toprint;

    public PrintCommand(String toprint, Process myprocess) {
        this.toprint = toprint;
        this.myprocess = myprocess;
    }

    public PrintCommand() {
        myprocess = null;
    }

    String getTYPE() {
        return TYPE;
    }

    @Override
    Process getMyprocess() {
        return null;
    }

    @Override
    Command validator(String[] input, Process myProcess, Process targetProcess) {
        if (input.length == 2) {
            return new PrintCommand(input[1], myProcess);
        } else {
            System.out.println("Error");
            return null;
        }
    }

    @Override
    int execute() {
        System.out.println("printed " + myprocess.id + ' ' + toprint + ' ' + myprocess.timer);
        return 0;
    }
}
