package ui;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private static final int BORDER = 48;
    private static final int PADDING = BORDER - 4;
    private static final Scanner scanner = new Scanner(System.in);
    private ArrayList<String> menuOptions;
    private int choice;
    private String topTitle;
    private String midTitle;
    private String menuInfo;
    private String exitOption;
    private String prePrompt;
    private String promptLine;

    public Menu() {
        this.topTitle = "";
        this.midTitle = "";
        this.menuInfo = "";
        this.exitOption = "Exit";
        this.menuOptions = new ArrayList<>();
        this.menuOptions.addFirst(this.exitOption);
       // this.menuOptions.add(0,this.exitOption);
        this.prePrompt = "";
        this.promptLine = "Enter: ";
    }

    public Menu(
            String topTitle,
            String midTitle,
            String menuInfo,
            String exitOption,
            ArrayList<String> menuOptions,
            String prePrompt,
            String promptLine
    ) {
        this.topTitle = topTitle;
        this.midTitle = midTitle;
        this.menuInfo = menuInfo;
        this.exitOption = exitOption;
        if (menuOptions.contains(this.exitOption)) {
            this.menuOptions = menuOptions;
        } else {
            this.menuOptions = menuOptions;
            this.menuOptions.addFirst(this.exitOption);
        }
        this.prePrompt = prePrompt;
        this.promptLine = promptLine;
    }

    public ArrayList<String> getMenuOptions() {
        return menuOptions;
    }

    public void setMenuOptions(ArrayList<String> menuOptions) {
        this.menuOptions = menuOptions;
    }

    public int getChoice() {
        return choice;
    }

    public void setTopTitle(String topTitle) {
        this.topTitle = topTitle;
    }

    public void setMidTitle(String midTitle) {
        this.midTitle = midTitle;
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

    public void drawMenuHeader() {
        // Klassen UI.ANSI innehåller olika typer av formatering. Varje UI.ANSI-property är en vanlig String.
        System.out.print(ANSI.CLEAR_SCREEN);

        String headerTop = topTitle;
        String topFlex = "─".repeat(PADDING);

        if (topTitle.isEmpty()) {
            headerTop = "─" + topFlex;
        } else {
            topFlex = "─".repeat(PADDING - headerTop.length() - 1);
            headerTop = " " + ANSI.BOLD + topTitle + ANSI.NO_BOLD + " ";
        }
        System.out.println(
            "\t╭─" + headerTop + topFlex + "╮");

        if (!midTitle.isEmpty()) {
            String centerFlex = " ".repeat((PADDING - midTitle.length()) / 2);
            String flexRest = " ".repeat((PADDING - midTitle.length()) % 2);
            System.out.println(
                "\t│ " + centerFlex + midTitle + centerFlex + flexRest + " │"
            );
        }
        System.out.println(
            "\t╰──" + "─".repeat(PADDING) + "╯"
        );
    }

    // Visar information under menyns titel. Man behöver inte ha någon information om man inte vill.
    public void drawMenuInfo() {
        if (!menuInfo.isEmpty()) {
            System.out.println("\t " + menuInfo);
        }
    }

    // Motsvarar raderna 19-25 i Nils BookController.showBookMenu()
    public void drawMenuOptions() {
        System.out.println(
            "\t╭──" + "─".repeat(PADDING) + "╮"
        );

        for (int i = 1; i < menuOptions.size(); i++) {
            String menuOption = menuOptions.get(i);
            String optionFlex = " ".repeat(PADDING - (menuOption.length() + 3));
            System.out.println(
                "\t│  " + ANSI.YELLOW + i + ". " + ANSI.DEFAULT_FG + menuOption + optionFlex + "│");
        }
        System.out.println("\t├──" + "─".repeat(PADDING) + "┤");

        String exitOptionFlex = " ".repeat(PADDING - (exitOption.length() + 3));
        System.out.println(
            "\t│  " + ANSI.BRIGHT_BLACK + 0 + ". " + ANSI.DEFAULT_FG + exitOption + exitOptionFlex + "│");
        System.out.println("\t╰──" + "─".repeat(PADDING) + "╯");
    }

    // Visar tips strax ovanför raden där man skriver sitt val
    public void drawPrePrompt() {
        System.out.println(
            ANSI.BRIGHT_BLACK + ANSI.ITALIC + "\t " + prePrompt + ANSI.NO_ITALIC + ANSI.DEFAULT_FG);
    }

    // Visar den text som finns på samma rad man skriver på. Typ, "Enter:"
    public void drawPromptLine() {
        System.out.print(
            "\t " + promptLine
        );
    }

    // Baseras på Nils Main och BookController-menyer
    public boolean showMenu() {
        boolean active = true;
        while (active) {
            drawMenuHeader();
            drawMenuInfo();
            drawMenuOptions();
            drawPrePrompt();
            drawPromptLine();
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (input >= 0 && input < menuOptions.size()) {
                    choice = input;
                    return input != 0;
                }
            } catch (NumberFormatException e) {
                System.out.println();
            }
        }
        return active;
    }
}
