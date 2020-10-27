package lamport;

/**
 * Timer super class to store process time
 *
 * @author Ritik Kumar <ritikkne@gmail.com>
 */

abstract class Timer {

    abstract void increment(int processIndex);
    abstract void decrement(int processIndex);

    abstract void updateTimer(Timer receivedTimer, int processNo);

}
