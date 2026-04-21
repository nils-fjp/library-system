package ui;

import java.util.LinkedHashMap;
import java.util.Map;

//import static member.MemberController.printer;

public class ConsolePrinter {

    public static void printHeader(String title) {
        System.out.println();
        System.out.println("\t" + ANSI.CYAN + ANSI.BOLD + title + ANSI.NO_BOLD + ANSI.DEFAULT_FG);
        System.out.println("\t" + ANSI.BRIGHT_BLACK + "────────────────────────────────────────" + ANSI.DEFAULT_FG);
    }

    public static void printFooter() {
        System.out.println("\t" + ANSI.BRIGHT_BLACK + "────────────────────────────────────────" + ANSI.DEFAULT_FG);
        System.out.println();
    }

    public static void printField(String label, Object value) {
        System.out.println("\t" + ANSI.YELLOW + label + ": " + ANSI.DEFAULT_FG + value);
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

    public static void printPrompt(String message) {
        System.out.println("\t" + ANSI.YELLOW + message + ANSI.DEFAULT_FG);
    }

    public static void printFields(String title, LinkedHashMap<String, Object> fields) {
        printHeader(title);

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            printField(entry.getKey(), entry.getValue());
        }

        printFooter();
    }


}
