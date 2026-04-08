package UI;

public class ConsolePrinter {

    public void printHeader(String title) {
        System.out.println();
        System.out.println("\t" + ANSI.CYAN + ANSI.BOLD + title + ANSI.NO_BOLD + ANSI.DEFAULT_FG);
        System.out.println("\t" + ANSI.BRIGHT_BLACK + "────────────────────────────────────────" + ANSI.DEFAULT_FG);
    }

    public void printFooter() {
        System.out.println("\t" + ANSI.BRIGHT_BLACK + "────────────────────────────────────────" + ANSI.DEFAULT_FG);
        System.out.println();
    }

    public void printField(String label, Object value) {
        System.out.println("\t" + ANSI.YELLOW + label + ": " + ANSI.DEFAULT_FG + value);
    }

    public void printError(String message) {
        System.out.println();
        System.out.println("\t" + ANSI.RED + message + ANSI.DEFAULT_FG);
        System.out.println();
    }

    public void printSuccess(String message) {
        System.out.println();
        System.out.println("\t" + ANSI.BRIGHT_GREEN + message + ANSI.DEFAULT_FG);
        System.out.println();
    }



}
