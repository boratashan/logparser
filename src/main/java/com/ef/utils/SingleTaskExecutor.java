package com.ef.utils;

/**
 * Executes a task under a seperated thread and provides function to query status of the task
 * and allow task to check if task interrupt is requested.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class SingleTaskExecutor {

    private ExecStatus execStatus;
    private Thread thread;
    private boolean isInterrupted = false;
    public SingleTaskExecutor() {
        execStatus = new ExecStatus(this);
    }

    public void execute(ExecuteHandler handler) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handler.executing(execStatus);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public boolean isInterrupted() {
        return this.isInterrupted;
    }

    public void interrupt() {
        this.isInterrupted = true;
    }

    public boolean isFinished() {
        return !(thread.isAlive());
    }

    public void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }

    public interface ExecuteHandler {
        public void executing(ExecStatus status) throws InterruptedException;
    }

    public class ExecStatus {
        private SingleTaskExecutor parent;

        public ExecStatus(SingleTaskExecutor parent) {
            this.parent = parent;
        }

        public boolean isInterrupted() {
            return parent.isInterrupted();
        }
    }
}
