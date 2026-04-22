package ui;

import java.util.LinkedHashMap;
import java.util.Map;

//import static member.MemberController.printer;

public class ConsolePrinter {
    private static final int MARGIN = Menu.MARGIN;
    private static final int OUTER_WIDTH = Menu.OUTER_WIDTH;
    private static final int GAP = Menu.GAP;

    public static void printHeaderCenter(String title) {
        title = fit(safe(title), OUTER_WIDTH);
        String flexLine = ANSI.BRIGHT_BLACK + "─".repeat((OUTER_WIDTH - title.length()) / 2) + " " + ANSI.DEFAULT_FG;
        String flexRest = ANSI.BRIGHT_BLACK + "─".repeat((OUTER_WIDTH - title.length()) % 2) + " " + ANSI.DEFAULT_FG;

        System.out.println();
        System.out.println(" ".repeat(MARGIN) + flexLine + ANSI.BOLD + title + ANSI.NO_BOLD + flexLine + flexRest + ANSI.DEFAULT_FG);
    }

    public static void printHeader(String title) {
        title = fit(safe(title), OUTER_WIDTH);
        String flexLine = ANSI.BRIGHT_BLACK + "─".repeat((OUTER_WIDTH - title.length()) / 2) + ANSI.DEFAULT_FG;

        System.out.println();
        System.out.println(" ".repeat(MARGIN) + flexLine + " " + ANSI.CYAN + ANSI.BOLD + title + ANSI.NO_BOLD + ANSI.DEFAULT_FG + " " + flexLine + ANSI.DEFAULT_FG);
        
    }

    public static void printFooter() {
        System.out.println(" ".repeat(MARGIN) + ANSI.BRIGHT_BLACK + "─".repeat(OUTER_WIDTH) + ANSI.DEFAULT_FG);
    }

    public static void printField(String label, Object value) {
        label = fit(safe(label), Math.max(0, GAP - 2));
        String flexGap = " ".repeat(Math.max(0, GAP - (label.length() + 2)));
        System.out.println(" ".repeat(MARGIN + 1) + ANSI.YELLOW + label + ": " + flexGap + ANSI.DEFAULT_FG + value);
    }

    public static void printPrompt(String message) {
        message = fit(safe(message), OUTER_WIDTH);
        String centerFlex = " ".repeat((OUTER_WIDTH - message.length()) / 4);
        System.out.println(" ".repeat(MARGIN) + ANSI.YELLOW + message + centerFlex + ANSI.DEFAULT_FG);
    }

    public static void printFields(String title, LinkedHashMap<String, Object> fields) {
        printHeader(title);

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            printField(entry.getKey(), entry.getValue());
        }

        printFooter();
    }

    public static void printError(String message) {
        message = safe(message);
        System.out.println();
        System.out.println("\t" + ANSI.RED + message + ANSI.DEFAULT_FG);
        System.out.println();
    }

    public static void printSuccess(String message) {
        message = safe(message);
        System.out.println();
        System.out.println("\t" + ANSI.BRIGHT_GREEN + message + ANSI.DEFAULT_FG);
        System.out.println();
    }

    private static String safe(String text) {
        return text == null ? "" : text;
    }

    private static String fit(String text, int maxWidth) {
        if (text.length() <= maxWidth) {
            return text;
        }

        return text.substring(0, Math.max(0, maxWidth));
    }

}
