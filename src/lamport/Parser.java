package lamport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Main Class to parse and execute
 *
 * @author Ritik Kumar <ritikkne@gmail.com>
 */


public final class Parser {
    private static final Pattern COMPILE = Pattern.compile("\\s+");
    private static Hashtable<String, Process> processRegistry;

    private static void throwError(String type, String msg) {
        if (type == "A") {
            System.err.println("Assertion Error: " + msg);
        } else if (type == "V") {
            System.err.println("Validation Error: " + msg);
        } else {
            System.err.println(msg);
        }
    }

    public static void main(String[] args) {
        File file =
                new File("./src/lamport/input.txt");
        List<Process> processes = new ArrayList<>();
        processRegistry = new Hashtable<String, Process>();
        boolean qn = false;
        try {
            Scanner sc;
            try {
                sc = new Scanner(file);
            } catch (FileNotFoundException e) {
                System.out.println("Error parsing");
                return;
            }

            boolean started = false;
            Process process = null;

            if (sc.hasNextLine()) {
                String qnInput = sc.nextLine();
                if ("1".equals(qnInput)) {
                    qn = true;
                } else if ("2".equals(qnInput)) {
                    qn = false;
                } else {
                    System.out.println("Error");
                    return;
                }
            }

            while (sc.hasNextLine()) {
                String asd = sc.nextLine();
                String[] splitStr = COMPILE.split(asd);
                if (splitStr.length == 1) {
                    continue;
                }

//            Process
                if ("begin".equals(splitStr[0]) && "process".equals(splitStr[1])) {
                    if (started) {
                        throwError("A", "Invalid Input");
                        return;
                    }
                    started = true;
                    process = Process.getOrCreate(splitStr[2]);
                    processes.add(process);
                    processRegistry.put(splitStr[2], process);
                } else if ("print".equals(splitStr[0])) {
                    if (!started) {
                        throwError("A", "Invalid Input");
                        return;
                    }

                    process.inputCommands.add(splitStr);

                } else if ("send".equals(splitStr[0])) {
                    if (!started) {
                        throwError("A", "Invalid Input");
                        return;
                    }
                    process.inputCommands.add(splitStr);

                } else if ("recv".equals(splitStr[0])) {
                    if (!started) {
                        throwError("A", "Invalid Input");
                        return;
                    }
                    process.inputCommands.add(splitStr);

                } else if ("end".equals(splitStr[0])) {
                    if (!started) {
                        throwError("A", "Invalid Input");
                        return;
                    }
                    started = false;

                } else {
                    throwError("A", "Invalid Input");
                    return;
                }
            }
            if (started) {
                throwError("A", "Invalid Input");
                return;
            }

            boolean valid = validateCommands(processes);
            if (!valid) {
                throwError("V", "Input Validation failed");
                return;
            }
        } catch (Exception ex) {
            throwError("A", "Invalid Input");
        }

        initiateProcesses(processes, qn);

        execute(processes);
    }

    private static void initiateProcesses(List<Process> processes, boolean logicalTimer) {
        int index = 0;
        for (Process p : processes) {
            p.initiate(processes.size(), index, logicalTimer);
            index++;
        }
    }

    private static void execute(List<Process> processes) {
        int count = 0;
        while (!processes.isEmpty() && count < 15) {
            if (checkProcessInDeadlock(processes)) {
                throwError("D", "system Deadlocked");
                printDeadlock(processes);
                break;
            }
            count++;
            Random r = new Random();
            int rand = r.nextInt(processes.size());

            Process p = processes.get(rand);

            p.executeNext();
            if (p.commandsDue.isEmpty()) {
                processes.remove(rand);
            }
        }
    }

    private static boolean validateCommands(List<Process> processes) {
        if (processes.size() > 9) {
            return false;
        }
//        Try Catch
        for (Process p : processes) {
            for (String[] input : p.inputCommands) {
                Command pc = Command.getCommandType(input[0]);
                if (pc instanceof PrintCommand) {
                    pc = pc.validator(input, p, null);
                } else {
                    Process target = getProcessByName(input[1]);
                    if (target == null) {
                        throwError(" ", "Invalid Process");
                        return false;
                    } else {
                        pc = pc.validator(input, p, target);
                    }
                }
                if (pc == null) {
                    return false;
                }
                p.commandsDue.add(pc);
            }
        }
        return true;
    }

    private static Process getProcessByName(String name) {
        if (processRegistry.containsKey(name)) {
            return processRegistry.get(name);
        }
        return null;
    }

    private static boolean checkProcessInDeadlock(List<Process> allProcesses) {
        for (Process p : allProcesses) {
            if (p.blocked == null) {
                return false;
            }
        }
        return true;
    }

    private static void printDeadlock(List<Process> allProcesses) {
        System.out.println();
        System.out.println("These processes are in deadlock:");

        for (Process p : allProcesses) {
            System.out.print(p.id + " : " + ((RecvCommand) p.commandsDue.get(0)).messageToRecv);
        }
        System.out.println();
    }
}

