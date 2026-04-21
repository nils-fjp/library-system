package ui;

import java.util.LinkedHashMap;
import java.util.Map;

//import static member.MemberController.printer;

public class ConsolePrinter {
    private static final int BORDER = 48;
    private static final int PADDING = BORDER - 4;

    public static void printHeader(String title) {
        System.out.println();
        String centerFlex = " ".repeat((BORDER - title.length()) / 2);
        String flexRest = " ".repeat((BORDER - title.length()) % 2);
        System.out.println("\t" + ANSI.CYAN + ANSI.BOLD + centerFlex + title + flexRest + ANSI.NO_BOLD + ANSI.DEFAULT_FG);
        System.out.println("\t" + ANSI.BRIGHT_BLACK + "─".repeat(BORDER) + ANSI.DEFAULT_FG);
    }

    public static void printFooter() {
        System.out.println("\t" + ANSI.BRIGHT_BLACK + "─".repeat(BORDER) + ANSI.DEFAULT_FG);
        System.out.println();
    }

    public static void printField(String label, Object value) {
        String centerFlex = " ".repeat((BORDER - label.length()) / 3);
        System.out.println("\t" + ANSI.YELLOW + label + ": " + centerFlex + ANSI.DEFAULT_FG + value);
    }

    public static void printPrompt(String message) {
        String centerFlex = " ".repeat((BORDER - message.length()) / 3);
        System.out.println("\t" + ANSI.YELLOW + message + centerFlex + ANSI.DEFAULT_FG);
    }

    public static void printFields(String title, LinkedHashMap<String, Object> fields) {
        printHeader(title);

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            printField(entry.getKey(), entry.getValue());
        }

        printFooter();
    }

    public static void printError(String message) {
        System.out.println();
        System.out.println("\t" + ANSI.RED + message + ANSI.DEFAULT_FG);
        System.out.println();
    }

    public static void printSuccess(String message) {
        System.out.println();
        System.out.println("\t" + ANSI.BRIGHT_GREEN + message + ANSI.DEFAULT_FG);
        System.out.println();
    }

}
