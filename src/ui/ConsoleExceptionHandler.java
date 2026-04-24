package ui;

import java.sql.SQLException;

public final class ConsoleExceptionHandler {

    private ConsoleExceptionHandler() {
    }

    public static void print(Exception exception) {
        if (exception instanceof SQLException) {
            ConsolePrinter.printError("Database error: " + exception.getMessage());
            return;
        }

        ConsolePrinter.printError(exception.getMessage());
    }
}
