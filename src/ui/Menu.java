package ui;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.ANSI.*;
import static ui.ConsolePrinter.*;
import static ui.Constants.*;

public class Menu {
    private static final String NEXT_PAGE_TEXT = BOLD + "Next [" + BRIGHT_BLACK + "Enter" + DEFAULT_FG + "]" + NO_BOLD;
    private static final Scanner scanner = new Scanner(System.in);
    private ArrayList<String> menuOptions;
    private int choice;
    private int currentOptionsPage;
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
        this.currentOptionsPage = 0;
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
        this.currentOptionsPage = 0;
        this.prePrompt = prePrompt;
        this.promptLine = promptLine;
    }

    private static void drawTopBorderRow(String topText) {
        topText = fit(safe(topText), INNER_WIDTH - 3);
        String topFlex = "─".repeat(Math.max(0, INNER_WIDTH - visibleLength(topText) - 3));
        String topBorder = "╭─ " + BOLD + topText + NO_BOLD + " " + topFlex + "╮";
        System.out.print(topBorder.indent(MARGIN));
    }

    private static void drawTopBorderRow() {
        String topBorder = "╭" + "─".repeat(INNER_WIDTH) + "╮";
        System.out.print(topBorder.indent(MARGIN));
    }

    private static void drawBlankRow() {
        String centerText = "│" + " ".repeat(INNER_WIDTH) + "│";
        System.out.print(centerText.indent(MARGIN));
    }

    private static void drawCenteredRow(String centerText) {
        centerText = safe(centerText);
        if (!centerText.isEmpty()) {
            centerText = "│" + center(BOLD + centerText + NO_BOLD, INNER_WIDTH) + "│";
        } else {
            drawBlankRow();
            return;
        }
        System.out.print(centerText.indent(MARGIN));
    }

    private static void drawBottomBorderRow() {
        String bottomBorder = "╰" + "─".repeat(INNER_WIDTH) + "╯";
        System.out.print(bottomBorder.indent(MARGIN));
    }

    private static void drawBottomBorderRow(String bottomText) {
        bottomText = safe(bottomText);
        if (!bottomText.isEmpty()) {
            bottomText = fit(bottomText, INNER_WIDTH - 3);
            String bottomFlex = "─".repeat(Math.max(0, INNER_WIDTH - visibleLength(bottomText) - 3));
            bottomText = "╰" + bottomFlex + " " + bottomText + " ─╯";
        } else {
            drawBottomBorderRow();
            return;
        }
        System.out.print(bottomText.indent(MARGIN));
    }

    private static void drawSeparatorRow() {
        String separator = "├" + "─".repeat(INNER_WIDTH) + "┤";
        System.out.print(separator.indent(MARGIN));
    }

    private static void drawOptionRow(int optionNumber, String optionText) {
        optionText = safe(optionText);
        String optionColor = optionNumber < 1 ? BRIGHT_BLACK : YELLOW;
        String optionPrefix = optionNumber + ". ";
        int optionTextWidth = Math.max(0, INNER_WIDTH - (optionPrefix.length() + PADDING * 2));
        optionText = padRight(optionText, optionTextWidth);
        optionText = "│" + " ".repeat(PADDING) + optionColor + optionPrefix + DEFAULT_FG + optionText + " ".repeat(
                PADDING) + "│";
        System.out.print(optionText.indent(MARGIN));
    }

    public static String formatInfoColumns(String leftText, String rightText) {
        return formatTwoColumnLine(leftText, rightText, INNER_WIDTH);
    }

    public ArrayList<String> getMenuOptions() {
        return menuOptions;
    }

    public void setMenuOptions(ArrayList<String> menuOptions) {
        this.menuOptions = menuOptions;
        normalizeCurrentOptionsPage();
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

    public void addMenuOption(String menuOption) {
        this.menuOptions.add(menuOption);
    }

    private void drawInfoRow(String line, boolean centered) {
        line = safe(line);

        if (line.isEmpty()) {
            System.out.print((" " + " ".repeat(INNER_WIDTH) + " ").indent(MARGIN));
            return;
        }

        if (centered) {
            line = " " + center(line, INNER_WIDTH) + " ";
        } else {
            line = " " + padRight(line, INNER_WIDTH) + " ";
        }

        System.out.print(line.indent(MARGIN));
    }

    public void drawPrePrompt(String prePrompt) {
        prePrompt = safe(prePrompt);
        System.out.println(" ".repeat(MARGIN + 1) + BRIGHT_BLACK + ITALIC + prePrompt + NO_ITALIC + DEFAULT_FG);
    }

    public void drawPromptLine(String promptLine) {
        promptLine = safe(promptLine);
        System.out.print(" ".repeat(MARGIN + 1) + promptLine);
    }

    public boolean showMenu() {
        String defaultPrePrompt = safe(this.prePrompt);

        boolean showing = true;
        while (showing) {
            normalizeCurrentOptionsPage();
            int selectableOptionCount = this.menuOptions.size() - 1;
            int optionsPageCount = Math.max(1, (selectableOptionCount + OPTIONS_PER_PAGE - 1) / OPTIONS_PER_PAGE);
            String bottomText = selectableOptionCount > OPTIONS_PER_PAGE ? NEXT_PAGE_TEXT : "";
            int startIndex = 1 + this.currentOptionsPage * OPTIONS_PER_PAGE;
            int endExclusive = Math.min(this.menuOptions.size(), startIndex + OPTIONS_PER_PAGE);

            System.out.print(CLEAR_SCREEN);
            drawTopBorderRow(this.topTitle);
            drawCenteredRow(safe(this.mainTitle).toUpperCase());
            drawBottomBorderRow();

            String[] infoLines = safe(this.menuInfo).split("\\R", -1);
            if (infoLines.length == 1) {
                drawInfoRow(infoLines[0], true);
            } else {
                for (String line : infoLines) {
                    drawInfoRow(line, false);
                }
            }

            drawTopBorderRow();
            if (this.currentOptionsPage > 0) {
                bottomText = NEXT_PAGE_TEXT;
                drawCenteredRow("Select an item to view details or see more options.");
            }
            for (int i = startIndex; i < endExclusive; i++) {
                drawOptionRow(i, this.menuOptions.get(i));
            }
            drawSeparatorRow();
            drawOptionRow(0, this.exitOption);
            drawBottomBorderRow(bottomText);
            drawPrePrompt(this.prePrompt);
            drawPromptLine(this.promptLine);

            String input = scanner.nextLine().trim();
            if (input.isEmpty() && selectableOptionCount > OPTIONS_PER_PAGE) {
                setPrePrompt(defaultPrePrompt);
                this.currentOptionsPage = (this.currentOptionsPage + 1) % optionsPageCount;
                continue;
            }

            try {
                int parsedInput = Integer.parseInt(input);
                if (parsedInput >= 0 && parsedInput < menuOptions.size()) {
                    choice = parsedInput;
                    setPrePrompt(defaultPrePrompt);
                    showing = parsedInput != 0;
                    break;
                }
            } catch (NumberFormatException e) {
                setPrePrompt(BRIGHT_RED + "Invalid input!" + BRIGHT_BLACK + " Please enter a valid number." + NO_ITALIC + DEFAULT_FG);
                continue;
            }

            setPrePrompt(BRIGHT_RED + "Invalid input!" + BRIGHT_BLACK + " Please enter a valid number." + NO_ITALIC + DEFAULT_FG);
        }
        return showing;
    }

    private void normalizeCurrentOptionsPage() {
        int lastOptionsPage = 0;
        if (this.menuOptions.size() > 1) {
            lastOptionsPage = (this.menuOptions.size() - 2) / OPTIONS_PER_PAGE;
        }
        this.currentOptionsPage = Math.min(this.currentOptionsPage, lastOptionsPage);
    }
}
