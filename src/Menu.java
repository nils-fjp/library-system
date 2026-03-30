import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private static final int BORDER = 48;
    private static final int PADDING = BORDER - 4;
    private static final Scanner scanner = new Scanner(System.in);
    private final ArrayList<String> menuOptions;
    private int choice;
    private String title;
    private String subtitle;
    private String menuInfo;
    private String exitOption;
    private String prePrompt;
    private String promptLine;

    public Menu() {
        this.title = "";
        this.subtitle = "";
        this.menuInfo = "";
        this.exitOption = "Exit";
        this.menuOptions = new ArrayList<>();
        this.menuOptions.addFirst(this.exitOption);
        this.prePrompt = "";
        this.promptLine = "Enter: ";
    }

    //
    public int getChoice() {
        return choice;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setMenuInfo(String menuInfo) {
        this.menuInfo = menuInfo;
    }

    public void addMenuOption(String menuOption) {
        this.menuOptions.add(menuOption);
    }

    public void setExitOption(String exitOption) {
        this.exitOption = exitOption;
        this.menuOptions.set(0, exitOption);
    }

    public void setPrePrompt(String prePrompt) {
        this.prePrompt = prePrompt;
    }

    public void setPromptLine(String promptLine) {
        this.promptLine = promptLine;
    }

    private void drawMenuHeader() {
        // Klassen ANSI innehåller olika typer av formatering. Varje ANSI-property är en vanlig String.
        System.out.print(ANSI.CLEAR_SCREEN);

        String headerTitle = title;
        String titleFlex = "─".repeat(PADDING);

        if (title.isEmpty()) {
            headerTitle = "─";
        } else {
            titleFlex = "─".repeat(PADDING - headerTitle.length() - 1);
            headerTitle = " " + ANSI.BOLD + title + ANSI.NO_BOLD + " ";
        }
        System.out.println(
            "  ╭─" + headerTitle + titleFlex + "╮");

        if (!subtitle.isEmpty()) {
            String centerFlex = " ".repeat((PADDING - subtitle.length()) / 2);
            System.out.println(
                "  │ " + centerFlex + subtitle + centerFlex + " │"
            );
        }
        System.out.println(
            "  ╰──" + "─".repeat(PADDING) + "╯"
        );
    }

    // Visar information under menyns titel. Man behöver inte ha någon information om man inte vill.
    private void drawMenuInfo() {
        if (!menuInfo.isEmpty()) {

            System.out.println("  " + menuInfo);
        }
    }

    // Motsvarar raderna 19-25 i Nils BookController.showBookMenu()
    private void drawMenuOptions() {
        System.out.println(
            "  ╭──" + "─".repeat(PADDING) + "╮"
        );

        for (int i = 1; i < menuOptions.size(); i++) {
            String menuOption = menuOptions.get(i);
            String optionFlex = " ".repeat(PADDING - (menuOption.length() + 3));
            System.out.println(
                "  │  " + "\033[1m" + i + ". " + ANSI.DEFAULT_FG + menuOption + optionFlex + "│");
        }
        System.out.println("  ├──" + "─".repeat(PADDING) + "┤");

        String exitOptionFlex = " ".repeat(PADDING - (exitOption.length() + 3));
        System.out.println(
            "  │  " + ANSI.BRIGHT_BLACK + 0 + ". " + ANSI.DEFAULT_FG + exitOption + exitOptionFlex + "│");
        System.out.println("  ╰──" + "─".repeat(PADDING) + "╯");
    }

    // Visar tips strax ovanför raden där man skriver sitt val
    private void drawPrePrompt() {
        System.out.println(
            ANSI.BRIGHT_BLACK + "  " + prePrompt + ANSI.DEFAULT_FG);
    }

    // Visar den text som finns på samma rad man skriver på. Typ, "Enter:"
    private void drawPromptLine() {
        System.out.print(
            ANSI.BRIGHT_BLACK + "  " + promptLine + ANSI.DEFAULT_FG);
    }

    // Baseras på Nils Main och BookController-menyer
    public boolean showMenu() {
        boolean showing = true;
        while (showing) {

            drawMenuHeader();
            drawMenuInfo();
            drawMenuOptions();
            drawPrePrompt();
            drawPromptLine();

            int input;
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                continue;
            }

            if (input == 0) {
                choice = input;
                return false;
            }
            if (input > 0 && input < this.menuOptions.size()) {
                choice = input;
                return true;
            }
        }
        return showing;
    }
}
