package com.ef;

import com.ef.analyzer.LogFileAnalyzer;
import com.ef.db.DbInitializer;
import com.ef.db.DbLogFileLoader;
import com.ef.exceptions.ArgumentException;
import com.ef.exceptions.ConfigFileException;
import com.ef.exceptions.DatabaseException;
import com.ef.utils.ApplicationUtils;
import com.ef.utils.ConsoleUtils;
import com.ef.utils.FileUtils;
import com.ef.utils.TaskRunnable;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The Parser program.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class Parser {
    private static final int EXITCODE_SUCCESS = 0;
    private static final int EXITCODE_ARGUMENT_ERROR = 1;
    private static final int EXITCODE_FILE_NOT_FOUND = 2;
    private static final int EXITCODE_USER_INTERRUPTED = 3;
    private static final int EXITCODE_CONSOLE_ERROR = 4;
    private static final int EXITCODE_CONFIGFILE_ERROR = 5;
    private static final int EXITCODE_DBINIT_ERROR = 6;
    private static final int EXITCODE_DBINIT_UNKNOWN_ERROR = 99;

    private static CommandLineParams params = null;
    private static AppConfig appConfig;

    public static void main(String[] args) {
        int exitCode = EXITCODE_SUCCESS;
        ConsoleUtils.println("Running parser...");
        try {
            try {
                params = new CommandLineParams(args);
                appConfig = AppConfig.getConfig();
                String logFileName = params.getLogFilePath();
                logFileName = FileUtils.addPathToFileNameIfNeeded(ApplicationUtils.getJarRunningPath(), logFileName);
                ConsoleUtils.println(String.format("Reading log file \"%s\"", logFileName));
                if (!FileUtils.isFileExisted(logFileName))
                    throw new FileNotFoundException(String.format("File \"%s\" is not found!", logFileName));
                (new DbInitializer()).initDbObjects();
                ConsoleUtils.println("Press and enter 'q' for stop processes and quit");
                DbLogFileLoader logFileLoader = new DbLogFileLoader(logFileName);
                LogFileAnalyzer logFileAnalyzer = new LogFileAnalyzer(logFileName,
                        params.getStartDate(),
                        params.getCalculatedEndDate(),
                        params.getThreshold());

                logFileAnalyzer.run();
                boolean isInterrupted = waitTaskOrKeyToFinish(logFileAnalyzer, 'q');
                if (!isInterrupted) {
                  logFileLoader.run();
                  isInterrupted = waitTaskOrKeyToFinish(logFileLoader, 'q');
                }

                if (isInterrupted) {
                    exitCode = EXITCODE_USER_INTERRUPTED;
                    ConsoleUtils.println("Process is interrupted by the user");
                    return;
                }

            } catch (IOException | DatabaseException | ConfigFileException | ArgumentException e) {
                if (e instanceof ArgumentException) {
                    exitCode = EXITCODE_ARGUMENT_ERROR;
                    ConsoleUtils.println("ERROR : Please specify arguments!");
                    ConsoleUtils.println("ERROR : For example : --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100");

                } else if (e instanceof FileNotFoundException)
                    exitCode = EXITCODE_FILE_NOT_FOUND;
                else if (e instanceof IOException)
                    exitCode = EXITCODE_CONSOLE_ERROR;
                else if (e instanceof ConfigFileException)
                    exitCode = EXITCODE_CONFIGFILE_ERROR;
                else if (e instanceof DatabaseException)
                    exitCode = EXITCODE_DBINIT_ERROR;

                ConsoleUtils.println(String.format("ERROR : %s, Internal error : %s", e.getMessage(), e.getCause() != null ? e.getCause().getMessage() : "---"));
            } catch (Exception e) {
                ConsoleUtils.println(String.format("UNKNOWN ERROR : %s", e.getMessage()));
                exitCode = EXITCODE_DBINIT_UNKNOWN_ERROR;
            }
        } finally {
            ConsoleUtils.println("Parser END.");
            System.exit(exitCode);
        }
    }

    private static boolean waitTaskOrKeyToFinish(TaskRunnable task, char keyToQuit) throws IOException {
        boolean isInterrupted = false;
        while (!(task.isFinished())) {
            if (System.in.available() > 0) {
                int c = System.in.read();
                if (c == 'q') {
                    task.interrupt();
                    isInterrupted = true;
                }
            }
        }
        if (isInterrupted)
            task.join();
        return isInterrupted;
    }

}
