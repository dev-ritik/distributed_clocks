/*
author: Ritik Kumar
 */
package lamport;

abstract class Timer {

    abstract void increment(int processIndex);
    abstract void decrement(int processIndex);

    abstract void updateTimer(Timer receivedTimer, int processNo);

}
