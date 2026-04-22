package ui;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    protected static final int OUTER_WIDTH = 48;
    protected static final int INNER_WIDTH = OUTER_WIDTH - 2;
    protected static final int MARGIN = 4;
    protected static final int PADDING = 2;
    protected static final int GAP = OUTER_WIDTH / 2;
    private static final String ANSI_ESCAPE_REGEX = "\u001B\\[[;\\d?]*[ -/]*[@-~]";
    private static final Scanner scanner = new Scanner(System.in);
    private ArrayList<String> menuOptions;
    private int choice;
    private String entry;
    private String topTitle;
    private String mainTitle;
    private String menuInfo;
    private String exitOption;
    private String prePrompt;
    private String promptLine;

    public Menu() {
        this.topTitle = "";
        this.mainTitle = "";
        this.menuInfo = "";
        this.exitOption = "Back";
        this.menuOptions = new ArrayList<>();
        this.menuOptions.addFirst(this.exitOption);
        // this.menuOptions.add(0,this.exitOption);
        this.prePrompt = "Type a number and press enter...";
        this.promptLine = "Enter: ";
    }

    public Menu(
            String topTitle,
            String mainTitle,
            String menuInfo,
            String exitOption,
            ArrayList<String> menuOptions,
            String prePrompt,
            String promptLine
    ) {
        this.topTitle = topTitle;
        this.mainTitle = mainTitle;
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

    public static void drawTopBorder(String topText) {
        topText = fit(safe(topText), INNER_WIDTH - 3);
        String topFlex = "─".repeat(Math.max(0, INNER_WIDTH - visibleLength(topText) - 3));
        String topBorder = "╭─ " + ANSI.BOLD + topText + ANSI.NO_BOLD + " " + topFlex + "╮";
        System.out.print(topBorder.indent(MARGIN));
    }

    public static void drawTopBorder() {
        String topBorder = "╭" + "─".repeat(INNER_WIDTH) + "╮";
        System.out.print(topBorder.indent(MARGIN));
    }

    public static void drawCentered() {
        String centerText = "│" + " ".repeat(INNER_WIDTH) + "│";
        System.out.print(centerText.indent(MARGIN));
    }

    public static void drawCentered(String centerText) {
        centerText = safe(centerText);
        if (!centerText.isEmpty()) {
            centerText = fit(centerText, INNER_WIDTH);
            int visibleWidth = visibleLength(centerText);
            String centerFlex = " ".repeat((INNER_WIDTH - visibleWidth) / 2);
            String flexRest = " ".repeat((INNER_WIDTH - visibleWidth) % 2);
            centerText = "│" + centerFlex + ANSI.BOLD + centerText + ANSI.NO_BOLD + centerFlex + flexRest + "│";
        } else {
            centerText = "│" + " ".repeat(INNER_WIDTH) + "│";
        }
        System.out.print(centerText.indent(MARGIN));
    }

    public static void drawBottomBorder() {
        String bottomBorder = "╰" + "─".repeat(INNER_WIDTH) + "╯";
        System.out.print(bottomBorder.indent(MARGIN));
    }

    public static void drawBottomBorder(String bottomText) {
        bottomText = safe(bottomText);
        if (!bottomText.isEmpty()) {
            bottomText = fit(bottomText, INNER_WIDTH - 3);
            String bottomFlex = "─".repeat(Math.max(0, INNER_WIDTH - visibleLength(bottomText) - 3));
            bottomText = "╰" + bottomFlex + " " + ANSI.BOLD + bottomText + ANSI.NO_BOLD + " ─╯";
        } else {
            bottomText = "╰" + "─".repeat(INNER_WIDTH) + "╯";
        }
        System.out.print(bottomText.indent(MARGIN));
    }

    public static void drawMenuSeparator() {
        String separator = "├" + "─".repeat(INNER_WIDTH) + "┤";
        System.out.print(separator.indent(MARGIN));
    }

    public static void drawMenuOption(int optionNumber, String optionText) {
        optionText = safe(optionText);
        String optionColor = optionNumber < 1 ? ANSI.BRIGHT_BLACK : ANSI.YELLOW;
        String optionPrefix = optionNumber + ". ";
        int optionTextWidth = Math.max(0, INNER_WIDTH - (optionPrefix.length() + PADDING * 2));
        optionText = fit(optionText, optionTextWidth);
        String optionFlex = " ".repeat(Math.max(0, optionTextWidth - visibleLength(optionText)));
        optionText = "│" + " ".repeat(PADDING) + optionColor + optionPrefix + ANSI.DEFAULT_FG + optionText + optionFlex + " ".repeat(
                PADDING) + "│";
        System.out.print(optionText.indent(MARGIN));
    }

    public static String formatInfoColumns(String leftText, String rightText) {
        leftText = safe(leftText);
        rightText = safe(rightText);

        int rightWidth = Math.min(visibleLength(rightText), Math.max(0, INNER_WIDTH - 1));
        int leftWidth = Math.max(0, INNER_WIDTH - rightWidth - 1);

        leftText = fit(leftText, leftWidth);
        int leftVisibleWidth = visibleLength(leftText);
        int spacesBetween = Math.max(1, INNER_WIDTH - leftVisibleWidth - rightWidth);

        return leftText + " ".repeat(spacesBetween) + rightText;
    }

    private static String safe(String text) {
        return text == null ? "" : text;
    }

    private static String fit(String text, int maxWidth) {
        if (visibleLength(text) <= maxWidth) {
            return text;
        }

        StringBuilder fitted = new StringBuilder();
        int visibleCount = 0;

        for (int i = 0; i < text.length() && visibleCount < maxWidth; ) {
            if (text.charAt(i) == '\u001B') {
                int sequenceEnd = findAnsiSequenceEnd(text, i);
                fitted.append(text, i, sequenceEnd);
                i = sequenceEnd;
                continue;
            }

            fitted.append(text.charAt(i));
            i++;
            visibleCount++;
        }

        return fitted.toString();
    }

    private static int visibleLength(String text) {
        return safe(text).replaceAll(ANSI_ESCAPE_REGEX, "").length();
    }

    private static int findAnsiSequenceEnd(String text, int startIndex) {
        int i = startIndex + 1;
        if (i >= text.length() || text.charAt(i) != '[') {
            return Math.min(text.length(), startIndex + 1);
        }

        i++;
        while (i < text.length()) {
            char current = text.charAt(i++);
            if (current >= '@' && current <= '~') {
                break;
            }
        }

        return i;
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

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
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

    public void drawMenuHeader(String topTitle, String mainTitle) {
        // Klassen UI.ANSI innehåller olika typer av formatering. Varje UI.ANSI-property är en vanlig String.
        drawTopBorder(topTitle);
        drawCentered(safe(mainTitle).toUpperCase());
        drawBottomBorder();
    }

    // Visar information under menyns titel. Man behöver inte ha någon information om man inte vill.
    public void drawMenuInfo(String menuInfo) {
        menuInfo = safe(menuInfo);
        String[] lines = menuInfo.split("\\R", -1);

        if (lines.length == 1) {
            drawMenuInfoLine(lines[0], true);
            return;
        }

        for (String line : lines) {
            drawMenuInfoLine(line, false);
        }
    }

    private void drawMenuInfoLine(String line, boolean centered) {
        line = safe(line);

        if (line.isEmpty()) {
            System.out.print((" " + " ".repeat(INNER_WIDTH) + " ").indent(MARGIN));
            return;
        }

        line = fit(line, INNER_WIDTH);

        if (centered) {
            int visibleWidth = visibleLength(line);
            String centerFlex = " ".repeat((INNER_WIDTH - visibleWidth) / 2);
            String flexRest = " ".repeat((INNER_WIDTH - visibleWidth) % 2);
            line = " " + centerFlex + line + centerFlex + flexRest + " ";
        } else {
            String flexRest = " ".repeat(Math.max(0, INNER_WIDTH - visibleLength(line)));
            line = " " + line + flexRest + " ";
        }

        System.out.print(line.indent(MARGIN));
    }

    // Motsvarar raderna 19-25 i Nils BookController.showBookMenu()
    public void drawMenuOptions(ArrayList<String> menuOptions, String exitOption) {
        drawTopBorder();
        for (int i = 1; i < menuOptions.size(); i++) {
            String menuOption = menuOptions.get(i);
            drawMenuOption(i, menuOption);
        }
        drawMenuSeparator();
        drawMenuOption(0, exitOption);
        drawBottomBorder();
    }

    // Visar tips strax ovanför raden där man skriver sitt val
    public void drawPrePrompt(String prePrompt) {
        prePrompt = safe(prePrompt);
        System.out.println(" ".repeat(MARGIN + 1) + ANSI.BRIGHT_BLACK + ANSI.ITALIC + prePrompt + ANSI.NO_ITALIC + ANSI.DEFAULT_FG);
    }

    // Visar den text som finns på samma rad man skriver på. Typ, "Enter:"
    public void drawPromptLine(String promptLine) {
        promptLine = safe(promptLine);
        System.out.print(" ".repeat(MARGIN + 1) + promptLine);
    }

    // Baseras på Nils Main och BookController-menyer
    public boolean showMenu() {
        boolean showing = true;
        String defaultPrePrompt = safe(this.prePrompt);
        while (showing) {
            System.out.print(ANSI.CLEAR_SCREEN);
            drawMenuHeader(this.topTitle, this.mainTitle);
            drawMenuInfo(this.menuInfo);
            drawMenuOptions(this.menuOptions, this.exitOption);
            drawPrePrompt(this.prePrompt);
            drawPromptLine(this.promptLine);
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (input >= 0 && input < menuOptions.size()) {
                    choice = input;
                    setPrePrompt(defaultPrePrompt);
                    return input != 0;
                }

            } catch (NumberFormatException e) {
                setPrePrompt(ANSI.RED + "Invalid input!" + ANSI.BRIGHT_BLACK + " Please enter a valid number." + ANSI.NO_ITALIC + ANSI.DEFAULT_FG);
            }
        }
        return showing;
    }
}
