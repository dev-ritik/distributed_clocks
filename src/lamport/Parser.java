/*
author: Ritik Kumar
 */

package lamport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public final class Parser {
    private static final Pattern COMPILE = Pattern.compile("\\s+");
    private static Hashtable<String, Process> processRegistry;

    public static void main(String[] args) {
        File file =
                new File("./src/lamport/input.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error parsing");
            return;
        }

        boolean started = false;
        Process process = null;
        List<Process> processes = new ArrayList<>();
        processRegistry = new Hashtable<String, Process>();
        boolean qn = false;
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
                    System.out.println("Error");
                    return;
                }
                started = true;
                process = Process.getOrCreate(splitStr[2]);
                processes.add(process);
                processRegistry.put(splitStr[2], process);
            } else if ("print".equals(splitStr[0])) {
                if (!started) {
                    System.out.println("Error");
                    return;
                }

                process.inputCommands.add(splitStr);

            } else if ("send".equals(splitStr[0])) {
                if (!started) {
                    System.out.println("Error");
                    return;
                }
                process.inputCommands.add(splitStr);

            } else if ("recv".equals(splitStr[0])) {
                if (!started) {
                    System.out.println("Error");
                    return;
                }
                process.inputCommands.add(splitStr);

            } else if ("end".equals(splitStr[0])) {
                if (!started) {
                    System.out.println("Error");
                    return;
                }
                started = false;

            } else {
                System.out.println("Error");
                return;
            }
        }
        if (started) {
            System.out.println("Error");
            return;
        }

        boolean valid = verifyCommands(processes);
        if (!valid) {
            System.out.println("Error");
            return;
        }

        valid = initiateProcesses(processes, qn);
        if (!valid) {
            System.out.println("Error");
            return;
        }

        execute(processes);
    }

    private static boolean initiateProcesses(List<Process> processes, boolean logicalTimer) {
        int index = 0;
        for (Process p : processes) {
            p.initiate(processes.size(), index, logicalTimer);
            index++;
        }
        return true;
    }

    private static void execute(List<Process> processes) {
        int count = 0;
        while (!processes.isEmpty() && count < 15) {
            if (checkProcessInDeadlock(processes)) {
                System.out.println("system deadlocked");
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

    private static boolean verifyCommands(List<Process> processes) {
        for (Process p : processes) {
            for (String[] input : p.inputCommands) {
                Command pc = Command.getCommandType(input[0]);
                if (pc instanceof PrintCommand) {
                    pc = pc.validator(input, p, null);
                } else {
                    pc = pc.validator(input, p, getProcessByName(input[1]));
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
            if (p.waitingFor == null) {
                return false;
            }
        }
        return true;
    }

}

