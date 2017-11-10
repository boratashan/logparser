package com.ef.utils;

/**
 * Contract of runnable task classes.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public interface TaskRunnable {
    public boolean isFinished();

    public void run();

    public void interrupt();

    public void join();
}
