package ui;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.ANSI.*;
import static ui.ConsolePrinter.*;
import static ui.Constants.*;

public class Menu {
    private static final String NEXT_PAGE_TEXT = BOLD + "Next [" + BRIGHT_BLACK + "Enter" + DEFAULT_FG + "]" + NO_BOLD;
    private static final Scanner scanner = new Scanner(System.in);
    private int currentPage;
    private ArrayList<String> menuOptions;
    private int choice;
    private String temporaryPrePrompt;
    private String topTitle;
    private String mainTitle;
    private String menuInfo;
    private String exitOption;
    private String prePrompt;
    private RenderMode renderMode;
    private String promptLine;

    public Menu() {
        this.topTitle = "";
        this.mainTitle = "";
        this.menuInfo = "";
        this.exitOption = "Back";
        this.menuOptions = new ArrayList<>();
        this.menuOptions.addFirst(this.exitOption);
        this.choice = 0;
        this.currentPage = 0;
        this.prePrompt = "Type a number and press enter...";
        this.temporaryPrePrompt = "";
        this.promptLine = "Enter: ";
        this.renderMode = RenderMode.STANDARD;
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
        this.choice = 0;
        this.currentPage = 0;
        this.prePrompt = prePrompt;
        this.temporaryPrePrompt = "";
        this.promptLine = promptLine;
        this.renderMode = RenderMode.STANDARD;
    }

    private static void drawBlankBoxRow() {
        String centerText = "│" + " ".repeat(INNER_WIDTH) + "│";
        System.out.print(centerText.indent(MARGIN));
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

    private static void drawCenteredBoxRow(String centerText) {
        centerText = safe(centerText);
        if (centerText.isEmpty()) {
            drawBlankBoxRow();
            return;
        }

        System.out.print(("│" + center(BOLD + centerText + NO_BOLD, INNER_WIDTH) + "│").indent(MARGIN));
    }

    private static void drawBottomBorderRow() {
        System.out.print(("╰" + "─".repeat(INNER_WIDTH) + "╯").indent(MARGIN));
    }

    private static void drawBottomBorderRow(String bottomText) {
        bottomText = safe(bottomText);
        if (bottomText.isEmpty()) {
            drawBottomBorderRow();
            return;
        }

        bottomText = fit(bottomText, INNER_WIDTH - 3);
        String bottomFlex = "─".repeat(Math.max(0, INNER_WIDTH - visibleLength(bottomText) - 3));
        System.out.print(("╰" + bottomFlex + " " + bottomText + " ─╯").indent(MARGIN));
    }

    private static void drawSeparatorRow() {
        System.out.print(("├" + "─".repeat(INNER_WIDTH) + "┤").indent(MARGIN));
    }

    public static String formatInfoColumns(String leftText, String rightText) {
        return formatInfoColumns(leftText, rightText, INNER_WIDTH);
    }

    public static String formatInfoColumns(String leftText, String rightText, int width) {
        return formatTwoColumnLine(leftText, rightText, width);
    }

    public static String formatListColumns(int optionCount, String leftText, String rightText) {
        int contentWidth = Math.max(0, INNER_WIDTH - (String.valueOf(Math.max(1, optionCount)).length() + 2));
        return formatInfoColumns(leftText, rightText, contentWidth);
    }

    public static String formatDetailLine(String label, Object value) {
        return formatInfoColumns(label, value == null ? "" : value.toString());
    }

    private void drawOptionRow(int optionNumber, String optionText) {
        String optionColor = optionNumber < 1 ? BRIGHT_BLACK : YELLOW;
        String optionPrefix = buildOptionPrefix(optionNumber);
        int optionTextWidth = Math.max(0, INNER_WIDTH - (optionPrefix.length() + PADDING * 2));
        String line = "│"
                + " ".repeat(PADDING)
                + optionColor + optionPrefix + DEFAULT_FG
                + padRight(safe(optionText), optionTextWidth)
                + " ".repeat(PADDING)
                + "│";
        System.out.print(line.indent(MARGIN));
    }

    private void drawDisplayRow(String line, boolean centered) {
        line = safe(line);
        if (line.isEmpty()) {
            System.out.print((" ".repeat(INNER_WIDTH)).indent(MARGIN + 1));
            return;
        }

        String renderedLine = centered ? center(line, INNER_WIDTH) : padRight(line, INNER_WIDTH);
        System.out.print(renderedLine.indent(MARGIN + 1));
    }

    private void drawListRow(int optionNumber, String optionText) {
        String line = padRight(buildOptionPrefix(optionNumber) + safe(optionText), INNER_WIDTH);
        System.out.print(line.indent(MARGIN + 1));
    }

    public ArrayList<String> getMenuOptions() {
        return menuOptions;
    }

    public void setMenuOptions(ArrayList<String> menuOptions) {
        this.menuOptions = menuOptions;
        normalizeCurrentPage();
    }

    public void setMenuInfo(String menuInfo) {
        this.menuInfo = menuInfo;
        normalizeCurrentPage();
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

    public void setTemporaryPrePrompt(String temporaryPrePrompt) {
        this.temporaryPrePrompt = safe(temporaryPrePrompt);
    }

    public void setExitOption(String exitOption) {
        this.exitOption = exitOption;
        this.menuOptions.set(0, exitOption);
    }

    public void setPrePrompt(String prePrompt) {
        this.prePrompt = prePrompt;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
        normalizeCurrentPage();
    }

    public void setPromptLine(String promptLine) {
        this.promptLine = promptLine;
    }

    public void addMenuOption(String menuOption) {
        this.menuOptions.add(menuOption);
        normalizeCurrentPage();
    }

    public void drawPrePrompt(String prePrompt) {
        prePrompt = safe(prePrompt);
        System.out.println((" ".repeat(MARGIN + 1) + BRIGHT_BLACK + ITALIC + prePrompt + NO_ITALIC + DEFAULT_FG));
    }

    public void drawPromptLine(String promptLine) {
        System.out.print(" ".repeat(MARGIN + 1) + safe(promptLine));
    }

    public boolean showMenu() {
        boolean showing = true;

        while (showing) {
            normalizeCurrentPage();
            String bottomText = isPagingActive() ? NEXT_PAGE_TEXT : "";

            System.out.print(CLEAR_SCREEN);
            drawTopBorderRow(this.topTitle);
            drawCenteredBoxRow(safe(this.mainTitle).toUpperCase());
            drawBottomBorderRow();

            if (this.renderMode == RenderMode.LIST) {
                drawListDisplayArea();
                drawTopBorderRow();
                drawCenteredBoxRow(safe(this.menuInfo));
                drawSeparatorRow();
                drawOptionRow(0, this.exitOption);
                drawBottomBorderRow(bottomText);
            } else {
                drawStandardDisplayArea();
                drawTopBorderRow();
                for (int i = 1; i < this.menuOptions.size(); i++) {
                    drawOptionRow(i, this.menuOptions.get(i));
                }
                drawSeparatorRow();
                drawOptionRow(0, this.exitOption);
                drawBottomBorderRow(bottomText);
            }

            drawPrePrompt(this.temporaryPrePrompt.isEmpty() ? this.prePrompt : this.temporaryPrePrompt);
            drawPromptLine(this.promptLine);

            String input = scanner.nextLine().trim();
            if (input.isEmpty() && isPagingActive()) {
                this.temporaryPrePrompt = "";
                this.currentPage = (this.currentPage + 1) % getPageCount();
                continue;
            }

            try {
                int parsedInput = Integer.parseInt(input);
                if (parsedInput >= 0 && parsedInput < this.menuOptions.size()) {
                    choice = parsedInput;
                    this.temporaryPrePrompt = "";
                    showing = parsedInput != 0;
                    break;
                }
            } catch (NumberFormatException e) {
                this.temporaryPrePrompt = BRIGHT_RED + "Invalid input!" + BRIGHT_BLACK + " Please enter a valid number." + NO_ITALIC + DEFAULT_FG;
                continue;
            }

            this.temporaryPrePrompt = BRIGHT_RED + "Invalid input!" + BRIGHT_BLACK + " Please enter a valid number." + NO_ITALIC + DEFAULT_FG;
        }

        return showing;
    }

    private void drawStandardDisplayArea() {
        String[] infoLines = getInfoLines();
        int displayRows = getDisplayRowCount();
        int start = this.currentPage * displayRows;
        int end = Math.min(infoLines.length, start + displayRows);
        boolean centered = infoLines.length == 1;
        int drawnRows = 0;

        for (int i = start; i < end; i++) {
            drawDisplayRow(infoLines[i], centered);
            drawnRows++;
        }

        while (drawnRows < displayRows) {
            drawDisplayRow("", false);
            drawnRows++;
        }
    }

    private void drawListDisplayArea() {
        int displayRows = getDisplayRowCount();
        int startIndex = 1 + this.currentPage * displayRows;
        int endExclusive = Math.min(this.menuOptions.size(), startIndex + displayRows);
        int drawnRows = 0;

        for (int i = startIndex; i < endExclusive; i++) {
            drawListRow(i, this.menuOptions.get(i));
            drawnRows++;
        }

        while (drawnRows < displayRows) {
            drawDisplayRow("", false);
            drawnRows++;
        }
    }

    private String[] getInfoLines() {
        return safe(this.menuInfo).split("\\R", -1);
    }

    private int getDisplayRowCount() {
        if (this.renderMode == RenderMode.LIST) {
            return OPTIONS_PER_PAGE;
        }

        return Math.max(0, MENU_BODY_ROWS - getStandardFooterRowCount());
    }

    private int getPageCount() {
        int displayRowCount = getDisplayRowCount();
        if (displayRowCount <= 0) {
            return 1;
        }

        if (this.renderMode == RenderMode.LIST) {
            int itemCount = Math.max(0, this.menuOptions.size() - 1);
            return Math.max(1, (itemCount + displayRowCount - 1) / displayRowCount);
        }

        String[] infoLines = getInfoLines();
        return Math.max(1, (infoLines.length + displayRowCount - 1) / displayRowCount);
    }

    private boolean isPagingActive() {
        return getPageCount() > 1;
    }

    private int getStandardFooterRowCount() {
        return this.menuOptions.size() + 3;
    }

    private void normalizeCurrentPage() {
        this.currentPage = Math.min(this.currentPage, getPageCount() - 1);
    }

    private String buildOptionPrefix(int optionNumber) {
        return padRight(optionNumber + ".", getOptionPrefixWidth() - 1) + " ";
    }

    private int getOptionPrefixWidth() {
        return String.valueOf(Math.max(0, this.menuOptions.size() - 1)).length() + 2;
    }

    public enum RenderMode {
        STANDARD,
        LIST
    }
}
