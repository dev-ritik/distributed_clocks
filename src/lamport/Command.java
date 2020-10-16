/*
author: Ritik Kumar
 */
package lamport;

abstract class Command {
    public static String TYPE;
    private Process myprocess;

    public static Command getCommandType(String type) {
        if (type.equals(PrintCommand.TYPE)) {
            return new PrintCommand();
        } else if (type.equals(SendCommand.TYPE)) {
            return new SendCommand();
        } else if (type.equals(RecvCommand.TYPE)) {
            return new RecvCommand();
        } else {
            System.out.println("Error");
            return null;
        }
    }

    abstract String getTYPE();

    abstract Process getMyprocess();

    abstract Command validator(String[] input, Process myprocess, Process targetProcess);

    abstract int execute();
}
